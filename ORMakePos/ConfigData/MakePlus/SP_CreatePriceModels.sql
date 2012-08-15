--
-- Dumping routines for database 'Ergatel'
--
DELIMITER ;;

/*!50003 DROP PROCEDURE IF EXISTS `sp_CreatePriceModels` */;;
/*!50003 SET SESSION SQL_MODE=""*/;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `sp_CreatePriceModels`(
)
BEGIN
delete from rate_price_pivot;

insert into rate_price_pivot (PRICE_MODEL,STEP,TIER_FROM,TIER_TO,BEAT,FACTOR,CHARGE_BASE)
(
  select distinct PRICE_GROUP,1,0,999999,1,PRICE,60 from PRICE_MAP
);

delete from PRICE_MODEL;
insert into PRICE_MODEL (PRICE_MODEL,STEP,TIER_FROM,TIER_TO,BEAT,FACTOR,CHARGE_BASE)
(
  select PRICE_MODEL,STEP,TIER_FROM,TIER_TO,BEAT,FACTOR,CHARGE_BASE from rate_price_pivot
);

END */;;

DELIMITER ;

