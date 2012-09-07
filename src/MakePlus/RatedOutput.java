package MakePlus;

import OpenRate.adapter.jdbc.JDBCBatchOutputAdapter;
import OpenRate.adapter.jdbc.JDBCOutputAdapter;
import OpenRate.record.DBRecord;
import OpenRate.record.IRecord;
import OpenRate.utils.ConversionUtils;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 *
 * @author IanAdmin
 */
public class RatedOutput extends JDBCBatchOutputAdapter {
    // this is the CVS version info

    public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: RatedOutput.java,v $, $Revision: 1.19 $, $Date: 2011/08/24 13:32:43 $";
    private static ConversionUtils conv = new ConversionUtils();
    private static int DECIMAL_PLACES = 4;


    /** Creates a new instance of DBOutput */
    public RatedOutput() {
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
    public Collection<IRecord> procValidRecord(IRecord r) {
       
        Collection Outbatch = new ArrayList();
        CDRRecord tmpInRecord = (CDRRecord)r;
        DBRecord tmpDataRecord = new DBRecord();
        tmpDataRecord.setOutputColumnCount(37);
        tmpDataRecord.setOutputColumnString(0, tmpInRecord.Type);
        tmpDataRecord.setOutputColumnString(1, tmpInRecord.Service);
        tmpDataRecord.setOutputColumnString(2, tmpInRecord.Direction);
        tmpDataRecord.setOutputColumnString(3, tmpInRecord.Network);
        tmpDataRecord.setOutputColumnString(4, tmpInRecord.RemoteNetwork);
        tmpDataRecord.setOutputColumnString(5, tmpInRecord.RemoteSwitch);
        tmpDataRecord.setOutputColumnString(6, tmpInRecord.PortingPrefix);
        tmpDataRecord.setOutputColumnString(7, tmpInRecord.StartDate);
        tmpDataRecord.setOutputColumnString(8, tmpInRecord.GuidingKey);
        tmpDataRecord.setOutputColumnString(9, tmpInRecord.BNumberNorm);
        tmpDataRecord.setOutputColumnString(10, tmpInRecord.CLI);
        tmpDataRecord.setOutputColumnDouble(11, Double.valueOf(tmpInRecord.Duration));
        tmpDataRecord.setOutputColumnDouble(12, Double.valueOf(tmpInRecord.retailPrice));
        tmpDataRecord.setOutputColumnDouble(13, Double.valueOf(tmpInRecord.wholeSalePrice));
        tmpDataRecord.setOutputColumnDouble(14, Double.valueOf(tmpInRecord.localPolo));
        tmpDataRecord.setOutputColumnDouble(15, Double.valueOf(tmpInRecord.localRolo));
        tmpDataRecord.setOutputColumnDouble(16, Double.valueOf(tmpInRecord.localTransit));
        tmpDataRecord.setOutputColumnDouble(17, Double.valueOf(tmpInRecord.remotePolo));
        tmpDataRecord.setOutputColumnDouble(18, Double.valueOf(tmpInRecord.remoteRolo));
        tmpDataRecord.setOutputColumnDouble(19, Double.valueOf(tmpInRecord.remoteTransit));
        tmpDataRecord.setOutputColumnString(20, tmpInRecord.Reseller);
        tmpDataRecord.setOutputColumnString(21, tmpInRecord.FinancialCode);
        tmpDataRecord.setOutputColumnString(22, tmpInRecord.AggregationID);
        tmpDataRecord.setOutputColumnString(23, tmpInRecord.callReference);
        tmpDataRecord.setOutputColumnString(24, tmpInRecord.NumberType);
        tmpDataRecord.setOutputColumnString(25, tmpInRecord.Reseller);
        tmpDataRecord.setOutputColumnString(26, tmpInRecord.WholesaleDescription);
        tmpDataRecord.setOutputColumnString(27, tmpInRecord.RetailDescription);
        tmpDataRecord.setOutputColumnString(28, tmpInRecord.LocalCostDescription);
        tmpDataRecord.setOutputColumnString(29, tmpInRecord.RemoteCostDescription);
        tmpDataRecord.setOutputColumnString(30, tmpInRecord.LocalTransitDescription);
        tmpDataRecord.setOutputColumnString(31, tmpInRecord.RemoteTransitDescription);
        tmpDataRecord.setOutputColumnString(32, tmpInRecord.Platform);
        tmpDataRecord.setOutputColumnString(33, tmpInRecord.TimeBand);
        tmpDataRecord.setOutputColumnString(34, tmpInRecord.MSN);
        tmpDataRecord.setOutputColumnString(35, tmpInRecord.DestinationDescription);
        tmpDataRecord.setOutputColumnString(36, tmpInRecord.UID);
       
        Outbatch.add((IRecord) tmpDataRecord);

        return Outbatch;
    }


    /**
     * Handle any error records here so that they are ready to output making any
     * specific changes to the record that are necessary to make it ready for
     * output.
     */
    @Override
    public Collection<IRecord> procErrorRecord(IRecord r) {
        return null;
    }
}
