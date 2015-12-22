--init.lua
--Projekt: eGarden - automatyka ogrodowa
--(c) 2015
--
-- Objasnienia portow GPIO:
-- 0 - brak (chyba na user button)
-- 1 - odczyt stanu jaki jest aktualnie na porcie 2
-- 2 - zapis HIGH/LOW (zapalanie i gaszenie LED'a)
-- 3 - zapis HIGH/LOW (zapalanie i gaszenie ��tego LED'a (resetu)
-- 4 - odczyt temperatury
-- 5 - 15 - brak przypisa�
--
gpioUsed = "0111100000000000" -- 0 - UNUSED   1 - USED
gpioType = "1021011111111111" -- 0 - INPUT    1 - OUTPUT   2 - AUTOMAT
systemId = "000001"  -- jaki� identyfikator ca�ego systemu automatyki ogrodowej


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

-- ustalenie konfiguracji GPIO zaraz po uruchomieniu 
setGpioMode()

--na terazniejsze testy :
--zapalam ��tego leda, kt�ry sygnalizuje mi, �e
--system si� zrestartowa� (port: D3)

gpio.write(3,0)
tmr.delay(2000)
gpio.write(3,1)

wifi.setmode(wifi.STATION)
wifi.sta.config("AndroidAP","nkxp5499")

tmr.alarm(1, 1000, 1, function()
    if wifi.sta.getip()== nil then
        print("IP niedost�pne, czekam...")
    else
        tmr.stop(1)
        print("WITAM,")
        print("Jestem ESP8266")
        print("==========================================")
        print("Tryb WI-FI: " .. wifi.getmode())
        print("MAC adres: " .. wifi.sta.getmac())
        print("Konfiguracja WI-FI: "..wifi.sta.getip())
        print("==========================================")
    end
end)

-- co 5 sekund wysy�am stany GPIO:
tmr.alarm(2, 5000, 1, function() 
    dofile('komunikacja0.lua')
    --tmr.stop(2) 
end)

-- co 3 minuty wysy�am message "I'm alive" ;)
tmr.alarm(3, 180000, 1, function()
    dofile('komunikacjaC.lua')
    gpio.write(3,0)
    --tmr.stop(3)
end)
