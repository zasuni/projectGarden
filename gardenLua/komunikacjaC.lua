local msgType = "1" -- sending datas

function getInfo()
    local info = msgType .. mac .. gpioUsed .. gpioType .. systemId
    if info==nil then info = "Blad danych: komunikacjaC.lua->getInfo() " end
    return info
end

function komunikacja(info)
     local conn=net.createConnection(net.TCP, 0)
     
     conn:on("receive", function(conn, payload) 
       success = true
       print(payload) 
     end)
       
     conn:on("connection",function(conn, payload)  
       print("\nPodlaczony")        
       conn:send(info .. "\n")
     end)
     
     conn:on("sent", function(conn, payload) 
       print('\nWyslano') 
       conn:close()
     end)
     
     conn:on("disconnection", function(conn, payload) 
       print('\nRozlaczony') 
     end)
                      
     conn:connect(5030,"192.168.43.100")
end

komunikacja(getInfo())
