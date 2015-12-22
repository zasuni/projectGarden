--init.lua
--Projekt: eGarden - automatyka ogrodowa
--(c) 2015
--
-- Objasnienia portow GPIO:
-- ================================================================
--  0 - brak (chyba na user button)
--  1 - odczyt stanu jaki jest aktualnie na porcie 2
--  2 - zapis HIGH/LOW (zapalanie i gaszenie LED'a)
--  3 - zapis HIGH/LOW (zapalanie i gaszenie zoltego LED'a (resetu)
--  4 - odczyt temperatury
--  5 - brak
--  6 - brak
--  7 - brak
--  8 - brak
--  9 - brak
-- 10 - brak
-- 11 - brak
-- 12 - brak
-- 13 - brak
-- 14 - brak
-- 15 - brak
--
gpioUsed = "0111100000000000" -- 0 - UNUSED   1 - USED
gpioType = "1021011111111111" -- 0 - INPUT    1 - OUTPUT   2 - AUTOMAT
systemId = "000001"  -- jakis identyfikator calego systemu automatyki ogrodowej
t = require("ds18b20")
t.setup(4) -- temperatura jest jako odczyt na porcie 4

print("\nKonfiguracja portow: ")
function setGpioMode()
    for i=0,15 do
        portNo = string.sub(gpioUsed,i+1,i+1)
        portType = string.sub(gpioType,i+1,i+1) 
        if portNo=="1"and portType=="0" then
            gpio.mode(i,gpio.INPUT)
            print("Port ["..i.."] -> INPUT\n")
        end;
        if portNo=="1"and portType=="1" then
            gpio.mode(i,gpio.OUTPUT)
            print("Port ["..i.."] -> OUTPUT\n")
        end;
    end
end

function zwolnij()
    t = nil
    mac = nil
    gpioUsed = nil
    gpioType = nil
    systemId = nil
    msgType = nil
    ds18b20 = nil
    package.loaded["ds18b20"]=nil
end;

-- ustalenie konfiguracji GPIO zaraz po uruchomieniu 
setGpioMode()

--na terazniejsze testy :
--zapalam zoltego leda, ktory sygnalizuje mi, ze
--system sie zrestartowal (port: D3)

gpio.write(3,0)
tmr.delay(2000)
gpio.write(3,1)

wifi.setmode(wifi.STATION)
wifi.sta.config("eGarden","garden@123")

tmr.alarm(1, 1000, 1, function()
    if wifi.sta.getip()== nil then
        print("IP niedostepne, czekam...")
    else
        tmr.stop(1)
        mac = wifi.sta.getmac()
        print("WITAM,")
        print("Jestem ESP8266")
        print("==========================================")
        print("Tryb WI-FI: " .. wifi.getmode())
        print("MAC adres: " .. wifi.sta.getmac())
        print("Konfiguracja WI-FI: "..wifi.sta.getip())
        print("==========================================")
    end
end)

-- co 5 sekund wysylam stany GPIO:
tmr.alarm(2, 30000, 1, function() 
    dofile('komunikacja0.lua')
    --tmr.stop(2) 
end)

-- co 3 minuty wysylam message "I'm alive" ;)
tmr.alarm(3, 50000, 1, function()
    dofile('komunikacjaC.lua')
    gpio.write(3,0)
    --tmr.stop(3)
end)

--zwolnij();
