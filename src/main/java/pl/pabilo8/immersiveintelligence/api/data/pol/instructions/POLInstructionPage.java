package pl.pabilo8.immersiveintelligence.api.data.pol.instructions;

import pl.pabilo8.immersiveintelligence.api.data.pol.POLComputerMemory;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLKeywords;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLProcess;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.DataTypeWrapper;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLScript.POLInstruction;
import pl.pabilo8.immersiveintelligence.api.data.pol.POLTerminal;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 22.04.2022
 */
public class POLInstructionPage extends POLInstruction
{
	private final DataTypeWrapper text;

	public POLInstructionPage(IDataType rest)
	{
		super(1);
		this.text = new DataTypeWrapper(rest);
	}

	@Override
	public void execute(POLComputerMemory memory, POLTerminal terminal, POLProcess polProcess, int executionTime)
	{
		IDataType type = text.get(memory.packet);
		if(type instanceof DataTypeInteger)
			memory.setPage(((DataTypeInteger)type).value);
	}

	@Override
	public POLKeywords getKeyword()
	{
		return POLKeywords.PAGE;
	}
}
