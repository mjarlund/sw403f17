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
        String msg;
        switch (report.Type)
        {
            case FuncDCLInCompValErr:
                if(report.Message != null) msg=report.Message;
                reporter.Error(new IncompatibleValueException("aasd"));
                break;
            case IncompatibleTypesError:
                if (report.Message != null){
                    msg = report.Message;
                    reporter.Error(new IncompatibleValueException(msg));
                }
            default: break;
        }
    }
}
