package MakePlus;

import OpenRate.adapter.file.FlatFileOutputAdapter;

import OpenRate.record.FlatRecord;
import OpenRate.record.IRecord;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Output Adapter is reponsible for writing the completed records to the
 * target file.
 */
public class BalRejectOutputAdapter
  extends FlatFileOutputAdapter
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: BalRejectOutputAdapter.java,v $, $Revision: 1.2 $, $Date: 2011/04/28 14:48:46 $";

  /**
  * Constructor for SaudiBTBatchOutputAdapter.
  */
  public BalRejectOutputAdapter()
  {
    super();
  }

  /**
   * We transform the records here so that they are ready to output making any
   * specific changes to the record that are necessary to make it ready for
   * output.
   *
   * As we are using the FlatFileOutput adapter, we should transform the records
   * into FlatRecords, storing the data to be written using the SetData() method.
   * This means that we do not have to know about the internal workings of the
   * output adapter.
   *
   * Note that this is just undoing the transformation that we did in the input
   * adapter.
   */
  @Override
    public Collection<IRecord> procValidRecord(IRecord r)
    {
      return null;
    }

  /**
   * Handle any error records here so that they are ready to output making any
   * specific changes to the record that are necessary to make it ready for
   * output.
   */
  @Override
    public Collection<IRecord> procErrorRecord(IRecord r)
    {
      FlatRecord tmpOutRecord;
      BalanceRecord tmpInRecord;

      Collection<IRecord> Outbatch;
      Outbatch = new ArrayList<IRecord>();

      tmpOutRecord = new FlatRecord();
      tmpInRecord = (BalanceRecord)r;
      tmpOutRecord.SetData(tmpInRecord.OriginalData);

      Outbatch.add((IRecord)tmpOutRecord);

      return Outbatch;
    }
}
