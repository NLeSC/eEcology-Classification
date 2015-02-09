SELECT device_info_serial, date_time, latitude, longitude, altitude, 
       pressure, temperature, satellites_used, gps_fixtime, positiondop, 
       h_accuracy, v_accuracy, x_speed, y_speed, z_speed, speed_accuracy, 
       location, userflag
  FROM gps.uva_tracking_data101 
 WHERE date_time > '2010-06-29 0:00:00'
   AND date_time < '2010-06-30 0:00:00'   
   AND altitude IS NOT NULL
   AND device_info_serial IN (1);

