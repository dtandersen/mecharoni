package us.davidandersen.mecharoni.command;

public interface Command<REQ, RES>
{
	void setRequest(REQ request);

	void setResult(RES result);

	void execute();
}
