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
 * and fitness for a particular purpose are discplaimed. In no event shall
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
/* ========================== VERSION HISTORY =========================
 * $Log: PromoCalcPostRating.java,v $
 * Revision 1.1  2011/08/16 06:04:43  ian
 * Discounting initial version
 *
 * ====================================================================
 */
package MakePlus;

import OpenRate.record.IRecord;
import OpenRate.lang.DiscountInformation;
import OpenRate.process.AbstractBalanceHandlerPlugIn;
import OpenRate.utils.ConversionUtils;

/**
 * Implement the value bundle packs: If there is some bundle value left, and it
 * is enough to cover the whole of the call, we discount the whole of the
 * call.
 *
 * If we have an override rate, we subtract the override retail value of the call,
 * otherwise we subtract the retail value from the call.
 *
 * If there is not enough value to cover the whole of the call, we zero the
 * counter, but take subtract the counter value from the normal retail rate.
 * This has the effect that the user sees only "normal" retail rated values,
 * with the override rate values used internally.
 * 
 * ** Note that this only affects the retail rated value **
 */
public class PromoCalcPostRating extends AbstractBalanceHandlerPlugIn
{
  /**
   * CVS version info - Automatically captured and written to the Framework
   * Version Audit log at Framework startup. For more information
   * please <a target='new' href='http://www.open-rate.com/wiki/index.php?title=Framework_Version_Map'>click here</a> to go to wiki page.
   */
  public static String CVS_MODULE_INFO = "OpenRate, $RCSfile: PromoCalcPostRating.java,v $, $Revision: 1.1 $, $Date: 2011/08/16 06:04:43 $";

  // Used for calculating the validites of the counters
  private ConversionUtils conversionUtils = new ConversionUtils();

  // -----------------------------------------------------------------------------
  // ------------------ Start of inherited Plug In functions ---------------------
  // -----------------------------------------------------------------------------

 /**
  * This is called when a data record is encountered. You should do any normal
  * processing here.
  */
  @Override
  public IRecord procValidRecord(IRecord r) 
  {
    CDRRecord CurrentRecord;
    DiscountInformation tmpDiscInfo;

    CurrentRecord = (CDRRecord) r;

    // Used for balance creation
    long tmpStartDate = 0;
    long tmpEndDate = 0;
      
    if (CurrentRecord.RECORD_TYPE == CDRRecord.CDR_RECORD)
    {
      if (CurrentRecord.UsedDiscount.trim().isEmpty())
      {
        // no work to do - go home
        return r;
      }

      // ******************************** EUR **********************************
      if ((CurrentRecord.getRUMValue("EUR") > 0) || (CurrentRecord.getRUMValue("OEUR") > 0))
      {
        // Create the daily or monthly balance
        if (CurrentRecord.DiscountPeriod.equalsIgnoreCase("m"))
        {
          tmpStartDate = conversionUtils.getUTCMonthStart(CurrentRecord.EventStartDate);
          tmpEndDate = conversionUtils.getUTCMonthEnd(CurrentRecord.EventStartDate);
        }
        else if (CurrentRecord.DiscountPeriod.equalsIgnoreCase("d"))
        {
          tmpStartDate = conversionUtils.getUTCDayStart(CurrentRecord.EventStartDate);
          tmpEndDate = conversionUtils.getUTCDayEnd(CurrentRecord.EventStartDate);
        }

        // perform the discount on the original retail value
        tmpDiscInfo = discountConsumeRUM(CurrentRecord, CurrentRecord.UsedDiscount, CurrentRecord.BalanceGroup, "EUR", CurrentRecord.DiscountCounter, CurrentRecord.DiscountInitValue, tmpStartDate, tmpEndDate);

        // put the info back in the Record
        if (tmpDiscInfo.isDiscountApplied())
        {
          CurrentRecord.DiscountRUM = "EUR";
          CurrentRecord.DiscountRule = "ConsumeEUR";
          CurrentRecord.DiscountGranted = tmpDiscInfo.getDiscountedValue();
          CurrentRecord.RecId = tmpDiscInfo.getRecId();
          CurrentRecord.CounterCycle = tmpDiscInfo.getCounterId();

          // Discount the retail price only
          CurrentRecord.retailPrice = CurrentRecord.retailPrice - CurrentRecord.DiscountGranted;
          CurrentRecord.DiscountRule = CurrentRecord.DiscountRule + "*";
        }
      }
    }
    
    return r;
  }

  /**
  * This is called when a data record with errors is encountered. You should do
  * any processing here that you have to do for error records, e.g. stratistics,
  * special handling, even error correction!
  */
  @Override
  public IRecord procErrorRecord(IRecord r)
  {
    return r;
  }    
}
