select s.device_info_serial, s.date_time,
                s.speed, s.longitude, s.latitude, s.altitude, t.speed as tspeed, 
                a.index,(a.x_acceleration-d.x_o)/d.x_s as x_cal, 
                (a.y_acceleration-d.y_o)/d.y_s as y_cal, 
                (a.z_acceleration-d.z_o)/d.z_s as z_cal 
                FROM gps.ee_tracking_speed_limited s 
                LEFT join gps.ee_acceleration_limited a   
                ON (s.device_info_serial = a.device_info_serial AND s.date_time = a.date_time) 
                LEFT join gps.ee_tracker_limited d 
                ON a.device_info_serial = d.device_info_serial 
                LEFT join gps.get_uvagps_track_speed ('806', '2014-06-07 00:00:00', '2014-06-09 00:00:00') t 
                ON s.device_info_serial = t.device_info_serial and s.date_time = t.date_time 
                where s.device_info_serial = '806'  and 
                s.date_time >'2014-06-07 12:00:00'  and 
                s.date_time <  '2014-06-07 12:10:00' and 
                s.latitude is not null and s.userflag <> 1 
                order by  s.date_time, a.index

                limit 10;