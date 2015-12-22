local msgType = "0" -- I'm alive ;)
    
function getPinValues()
    for i=0,15 do
        isUsed = string.sub(gpioUsed,i+1,i+1)
        portType = string.sub(gpioType,i+1,i+1) 
        if isUsed=="1" and portType=="0" then
            if i==4 then stan=tostring(t.read()) else stan=tostring(gpio.read(i)) end;
            dl=string.len(stan)
            for a=1,8-dl do
                stan="0"..stan;
            end;
            if stan==nil then stan="0" end
            msg = msgType .. mac .. tostring(i) .. systemId .. stan .. "H:" ..tostring(node.heap())
            komunikacja(msg)
        end;
    end
end

function zmianaStanuLeda()
     pin=2
     stan=gpio.read(1)
     if stan==0 then stan=1 else stan=0 end
     gpio.write(pin,stan)
end
    
function komunikacja(info)
     local conn=net.createConnection(net.TCP, 0)
     
     conn:on("receive", function(conn, payload) 
       success = true
       if payload=="sendConfig" then doFile("komunikacjaC.lua") end; 
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

zmianaStanuLeda()
getPinValues()