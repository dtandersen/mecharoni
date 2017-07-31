package us.davidandersen.mecharoni.command;

public abstract class BaseCommand<REQ, RES> implements Command<REQ, RES>
{
	protected REQ request;

	protected RES result;

	public void setRequest(final REQ request)
	{
		this.request = request;
	}

	public void setResult(final RES result)
	{
		this.result = result;
	}
}
