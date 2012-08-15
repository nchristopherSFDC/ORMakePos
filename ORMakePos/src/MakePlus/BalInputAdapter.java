package MakePlus;

import OpenRate.adapter.file.FlatFileInputAdapter;

import OpenRate.record.FlatRecord;
import OpenRate.record.IRecord;

public class BalInputAdapter
  extends FlatFileInputAdapter
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: BalInputAdapter.java,v $, $Revision: 1.2 $, $Date: 2011/04/28 14:48:46 $";

  private int IntRecordNumber;

 /**
  * Constructor for CustomizeInputAdapter.
  */
  public BalInputAdapter()
  {
      super();
  }



  // -----------------------------------------------------------------------------
  // ------------------ Start of inherited Plug In functions ---------------------
  // -----------------------------------------------------------------------------
 /**
  * This is called when the synthetic Header record is encountered, and has the
  * meanining that the stream is starting. In this example we have nothing to do
  */
  @Override
  public IRecord procHeader(IRecord r)
  {
    IntRecordNumber = 0;

    return r;
  }

 /**
  * This is called when a data record is encountered. You should do any normal
  * processing here. For the input adapter, we probably want to change the
  * record type from FlatRecord to the record(s) type that we will be using in
  * the processing pipeline.
  *
  * This is also the location for accumulating records into logical groups
  * (that is records with sub records) and placing them in the pipeline as
  * they are completed. If you receive a sub record, simply return a null record
  * in this method to indicate that you are handling it, and that it will be
  * purged at a later date.
  */
  @Override
  public IRecord procValidRecord(IRecord r)
  {
    FlatRecord originalRecord = (FlatRecord)r;
    String  tmpData = originalRecord.GetData();

    BalanceRecord tmpDataRecord = new BalanceRecord(tmpData);
    if (tmpDataRecord.performFieldValidation(this.getSymbolicName()) == true)
    {
      tmpDataRecord.addOutput("DefaultOutput");
    }
    else
    {
      tmpDataRecord.addOutput("RejectOutput");
    }

    IntRecordNumber++;
    tmpDataRecord.RecordNumber = IntRecordNumber;


    // Return the modified record in the Common record format (IRecord)
    return (IRecord) tmpDataRecord;
  }

 /**
  * This is called when a data record with errors is encountered. You should do
  * any processing here that you have to do for error records, e.g. stratistics,
  * special handling, even error correction!
  *
  * The input adapter is not expected to provide any records here.
  */
  @Override
  public IRecord procErrorRecord(IRecord r)
  {
    // The FlatFileInputAdapter is not able to create error records, so we
    // do not have to do anything for this
    return r;
  }

 /**
  * This is called when the synthetic trailer record is encountered, and has the
  * meanining that the stream is now finished. In this example, all we do is
  * pass the control back to the transactional layer.
  */
  @Override
  public IRecord procTrailer(IRecord r)
  {

    return r;
  }
}
