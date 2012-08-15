package MakePlus;

import OpenRate.adapter.jdbc.JDBCBatchOutputAdapter;
import OpenRate.record.DBRecord;
import OpenRate.record.IRecord;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author IanAdmin
 */
public class BalDBOutputAdapter extends JDBCBatchOutputAdapter
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: BalDBOutputAdapter.java,v $, $Revision: 1.11 $, $Date: 2011/08/23 17:19:47 $";

  /** Creates a new instance of DBOutput */
  public BalDBOutputAdapter()
  {
    super();
  }

  /**
   * We transform the records here so that they are ready to output making any
   * specific changes to the record that are necessary to make it ready for
   * output.
   *
   * As we are using the JDBCOutput adapter, we should transform the records
   * into DBRecords, storing the data to be written using the SetData() method.
   * This means that we do not have to know about the internal workings of the
   * output adapter.
   *
   * Note that this is just undoing the transformation that we did in the input
   * adapter.
   */
    @Override
    public Collection<IRecord> procValidRecord(IRecord r)
    {
      DBRecord tmpDataRecord;
      BalanceRecord tmpInRecord;

      Collection<IRecord> Outbatch;
      Outbatch = new ArrayList<IRecord>();
      tmpInRecord = (BalanceRecord)r;
      tmpDataRecord = new DBRecord();
      tmpDataRecord.setOutputColumnCount(15);
      tmpDataRecord.setOutputColumnString(0,tmpInRecord.User);
      tmpDataRecord.setOutputColumnString(1,tmpInRecord.Date);
      tmpDataRecord.setOutputColumnString(2,tmpInRecord.AggID);
      tmpDataRecord.setOutputColumnString(3,tmpInRecord.Financial_Code);
      tmpDataRecord.setOutputColumnString(4,tmpInRecord.AliasStat);
      tmpDataRecord.setOutputColumnString(5,tmpInRecord.Network);
      tmpDataRecord.setOutputColumnString(6,tmpInRecord.Count);
      tmpDataRecord.setOutputColumnDouble(7,tmpInRecord.RetailCost);
      tmpDataRecord.setOutputColumnDouble(8,tmpInRecord.WholeSaleCost);
      tmpDataRecord.setOutputColumnDouble(9,tmpInRecord.RemotePolo);
      tmpDataRecord.setOutputColumnDouble(10,tmpInRecord.RemoteRolo);
      tmpDataRecord.setOutputColumnDouble(11,tmpInRecord.LocalPolo);
      tmpDataRecord.setOutputColumnDouble(12,tmpInRecord.LocalRolo);
      tmpDataRecord.setOutputColumnDouble(13,tmpInRecord.LocalTransit);
      tmpDataRecord.setOutputColumnDouble(14,tmpInRecord.RemoteTransit);

      Outbatch.add((IRecord)tmpDataRecord);

      return Outbatch;
    }

  /**
   * Handle any error records here so that they are ready to output making any
   * specific changes to the record that are necessary to make it ready for
   * output.
   */
    @Override
    public Collection<IRecord> procErrorRecord(IRecord r)
    {
      return null;
    }
}
