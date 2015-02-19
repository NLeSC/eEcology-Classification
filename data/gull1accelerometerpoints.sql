-- "device_info_serial","date_time","speed","longitude","latitude","altitude","tspeed","index","x_cal","y_cal","z_cal"
SELECT g.device_info_serial, g.date_time, g.speed_2d AS speed, g.longitude, g.latitude, g.altitude, g.speed_3d AS tspeed, a.index,
	       (a.x_acceleration-d.x_o)/d.x_s AS x_cal, 
               (a.y_acceleration-d.y_o)/d.y_s AS y_cal,
               (a.z_acceleration-d.z_o)/d.z_s AS z_cal 
  FROM gps.uva_tracking_data101 AS g
  JOIN gps.uva_acceleration101 a ON g.device_info_serial = a.device_info_serial AND g.date_time = a.date_time
  JOIN gps.ee_tracker d ON g.device_info_serial = d.device_info_serial
 WHERE g.device_info_serial=1
   AND g.date_time BETWEEN '2010-06-30 12:00:00' AND '2010-06-30 12:30:00'
 ORDER BY g.device_info_serial, g.date_time, a.index
   
  
