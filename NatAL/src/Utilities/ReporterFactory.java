package Utilities;

import Exceptions.IncompatibleValueException;

/**
 * Created by mysjkin on 5/3/17.
 */
public class ReporterFactory
{
    private Reporter reporter;
    public ReporterFactory(Reporter reporter)
    {
        this.reporter = reporter;
    }
    public void CreateError(Report report)
    {
        switch (report.Type)
        {
            case FuncDCLInCompValErr:
                String msg;
                if(report.Message != null) msg=report.Message;
                reporter.Error(new IncompatibleValueException("aasd"));
                break;
            default: break;
        }
    }
}
