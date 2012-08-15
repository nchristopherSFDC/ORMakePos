/* ====================================================================
 * Limited Evaluation License:
 *
 * The exclusive owner of this work is Tiger Shore Management Ltd.
 * This work, including all associated documents and components
 * is Copyright Tiger Shore Management Ltd 2006-2010.
 *
 * The following restrictions apply unless they are expressly relaxed in a
 * contractual agreement between the license holder or one of its officially
 * assigned agents and you or your organisation:
 *
 * 1) This work may not be disclosed, either in full or in part, in any form
 *    electronic or physical, to any third party. This includes both in the
 *    form of source code and compiled modules.
 * 2) This work contains trade secrets in the form of architecture, algorithms
 *    methods and technologies. These trade secrets may not be disclosed to
 *    third parties in any form, either directly or in summary or paraphrased
 *    form, nor may these trade secrets be used to construct products of a
 *    similar or competing nature either by you or third parties.
 * 3) This work may not be included in full or in part in any application.
 * 4) You may not remove or alter any proprietary legends or notices contained
 *    in or on this work.
 * 5) This software may not be reverse-engineered or otherwise decompiled, if
 *    you received this work in a compiled form.
 * 6) This work is licensed, not sold. Possession of this software does not
 *    imply or grant any right to you.
 * 7) You agree to disclose any changes to this work to the copyright holder
 *    and that the copyright holder may include any such changes at its own
 *    discretion into the work
 * 8) You agree not to derive other works from the trade secrets in this work,
 *    and that any such derivation may make you liable to pay damages to the
 *    copyright holder
 * 9) You agree to use this software exclusively for evaluation purposes, and
 *    that you shall not use this software to derive commercial profit or
 *    support your business or personal activities.
 *
 * This software is provided "as is" and any expressed or impled warranties,
 * including, but not limited to, the impled warranties of merchantability
 * and fitness for a particular purpose are disclaimed. In no event shall
 * Tiger Shore Management or its officially assigned agents be liable to any
 * direct, indirect, incidental, special, exemplary, or consequential damages
 * (including but not limited to, procurement of substitute goods or services;
 * Loss of use, data, or profits; or any business interruption) however caused
 * and on theory of liability, whether in contract, strict liability, or tort
 * (including negligence or otherwise) arising in any way out of the use of
 * this software, even if advised of the possibility of such damage.
 * This software contains portions by The Apache Software Foundation, Robert
 * Half International.
 * ====================================================================
 */

package MakePlus;

import OpenRate.exception.ProcessingException;
import OpenRate.process.AbstractRUMTimeMatch;
import OpenRate.process.AbstractStubPlugIn;
import OpenRate.record.ChargePacket;
import OpenRate.record.IRecord;

/**
 *
 * @author m@rco
 */
public class ChargePacketCreation extends AbstractStubPlugIn
{
  // this is the CVS version info
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: ChargePacketCreation.java,v $, $Revision: 1.6 $, $Date: 2011/07/22 08:42:37 $";

  @Override
  public IRecord procValidRecord(IRecord r) throws ProcessingException
  {
    CDRRecord CurrentRecord = (CDRRecord)r;

    // Create Retail CP
    ChargePacket cp_new = new ChargePacket();
    cp_new.TimeSplitting = AbstractRUMTimeMatch.TIME_SPLITTING_CHECK_SPLITTING;
    cp_new.PacketType = "R";
    cp_new.RatingType = ChargePacket.RATING_TYPE_TIERED;
    cp_new.Service = CurrentRecord.Service;

    // Set the time model - this is always the STANDARD rate plan (not specific)
    cp_new.TimeModel = CurrentRecord.BaseRateplan;

    // Set the zone model so we know what we based it on
    cp_new.ZoneResult = CurrentRecord.ZoneDestination;
    cp_new.ZoneModel = CurrentRecord.Service;

    // Set the user tariff
    cp_new.RatePlanName=CurrentRecord.UserTariff;
    cp_new.SubscriptionID=CurrentRecord.GuidingKey;
    
    // Set the duration
    cp_new.RUMName = "DUR";
    cp_new.Resource = "R";
    cp_new.RUMQuantity = CurrentRecord.Duration;

    // Add it
    CurrentRecord.addChargePacket(cp_new);

    // Wholesale
    ChargePacket lc_W = cp_new.Clone();
    lc_W.PacketType="W";
    lc_W.Resource="W";
    CurrentRecord.addChargePacket(lc_W);
    // set destination done back to bnumber zone
    cp_new.ZoneResult = CurrentRecord.ZoneDestination;
    // Local cost - use network specific rating
    ChargePacket lc_CP = cp_new.Clone();
    lc_CP.PacketType="LC";
    lc_CP.Resource="LC";
    lc_CP.RatePlanName = lc_CP.PacketType;
    CurrentRecord.addChargePacket(lc_CP);
    
    // local transit - use network specific rating
    ChargePacket lt_CP = cp_new.Clone();
    lt_CP.PacketType="LT";
    lt_CP.Resource="LT";
    CurrentRecord.addChargePacket(lt_CP);
    
    // remote cost - use network specific rating
    ChargePacket rc_CP = cp_new.Clone();
    rc_CP.PacketType="RC";
    rc_CP.Resource="RC";
    CurrentRecord.addChargePacket(rc_CP);
    
    // remote transit - use network specific rating
    ChargePacket rt_CP = cp_new.Clone();
    rt_CP.PacketType="RT";
    rt_CP.Resource="RT";
    CurrentRecord.addChargePacket(rt_CP);
    
    return r;
  }

  @Override
  public IRecord procErrorRecord(IRecord r) throws ProcessingException
  {
    return r;
  }
}