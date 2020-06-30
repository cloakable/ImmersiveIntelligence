package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;
import pl.pabilo8.immersiveintelligence.common.util.commands.tmt.CommandIIGiveBullet;
import pl.pabilo8.immersiveintelligence.common.util.commands.tmt.CommandTMTAnimControl;
import pl.pabilo8.immersiveintelligence.common.util.commands.tmt.CommandTMTReloadModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23-06-2019
 */
public class IICommandHandler extends CommandTreeBase
{
	private final String name;

	public IICommandHandler(String name)
	{
		this.name = name;

		switch(name)
		{
			case "tmt":

				addSubcommand(new CommandTMTReloadModels());
				addSubcommand(new CommandTMTAnimControl());
				break;
			case "ii":
				addSubcommand(new CommandIIGiveBullet());
				//addSubcommand(new CommandIIToggleInstantBuild());
				break;
		}

		addSubcommand(new CommandTreeHelp(this));
	}

	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Use \"/"+name+" help\" for more information";
	}

	private static final String start = "<";
	private static final String end = ">";

	/**
	 * Get a list of options for when the user presses the TAB key
	 */
	@Nonnull
	@Override
	public List<String> getTabCompletions(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		List<String> ret = super.getTabCompletions(server, sender, args, pos);
		for(int i = 0; i < ret.size(); i++)
		{
			String curr = ret.get(i);
			if(curr.indexOf(' ') >= 0)
			{
				ret.set(i, start+curr+end);
			}
		}
		return ret;
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException
	{
		List<String> argsCleaned = new ArrayList<>(args.length);
		String currentPart = null;
		for(String s : args)
		{
			if(s.startsWith(start))
			{
				if(currentPart!=null)
					throw new CommandException("String opens twice (once \""+currentPart+"\", once \""+s+"\")");
				currentPart = s;
			}
			else if(currentPart!=null)
				currentPart += " "+s;
			else
				argsCleaned.add(s);
			if(s.endsWith(end))
			{
				if(currentPart==null)
					throw new CommandException("String closed without being openeed first! (\""+s+"\")");
				if(currentPart.length() >= 2)
					argsCleaned.add(currentPart.substring(1, currentPart.length()-1));
				currentPart = null;
			}
		}
		if(currentPart!=null)
			throw new CommandException("Unclosed string ("+currentPart+")");
		super.execute(server, sender, argsCleaned.toArray(new String[0]));
	}
}
