package library.walidator;

import java.util.Arrays;

public enum TypWalidacji {
	NIE_PUSTY(
			TypCechy.KOMUNIKAT,
			TypCechy.DUZE_LITERY,
			TypCechy.MAX_ZNAKOW
			),
	
	LISTA(	
			TypCechy.KOMUNIKAT,
			TypCechy.ORGIN_VALUE,
			TypCechy.NIE_LISTA,
			TypCechy.DUZE_LITERY,
			TypCechy.MAX_ZNAKOW,
			TypCechy.POLE_PUSTE
			),
			
	REGEX(	
			TypCechy.KOMUNIKAT,
			TypCechy.PATTERN, 
			TypCechy.DUZE_LITERY,
			TypCechy.MAX_ZNAKOW, 
			TypCechy.POLE_PUSTE
			),
			
	INTEGER(TypCechy.KOMUNIKAT,
			TypCechy.MIN,
			TypCechy.MAX, 
			TypCechy.POLE_PUSTE
			),
			
	LONG(
			TypCechy.KOMUNIKAT, 
			TypCechy.MIN, 
			TypCechy.MAX, 
			TypCechy.POLE_PUSTE
			),
	
	DOUBLE(
			TypCechy.KOMUNIKAT, 
			TypCechy.PRECYZJA_SKALA, 
			TypCechy.MASKA, 
			TypCechy.MIN, 
			TypCechy.MAX, 
			TypCechy.POLE_PUSTE
			),
			
	TEXT_MIN_MAX(
			TypCechy.KOMUNIKAT, 
			TypCechy.MIN_ZNAKOW,
			TypCechy.MAX_ZNAKOW,
			TypCechy.POLE_PUSTE
			),
	
	INDEKS(	TypCechy.KOMUNIKAT,
			TypCechy.ORGIN_VALUE,
			TypCechy.PATTERN, 
			TypCechy.TEMP_PATTERN, 
			TypCechy.MASKA, 
			TypCechy.MAX_ZNAKOW, 
			TypCechy.DUZE_LITERY,
			TypCechy.POLE_PUSTE,
			TypCechy.ID
			),
			
	TEST_REGEX(	
			TypCechy.KOMUNIKAT,
			TypCechy.MAX_ZNAKOW, 
			TypCechy.POLE_PUSTE
			),
	
	LOGICZNY(
			TypCechy.KOMUNIKAT
			);
	
	private TypCechy[] cechy;

	private TypWalidacji(TypCechy... cechy) {
		this.cechy = cechy;
	}

	public String get() {
		return String.format("%10s: %s", this, Arrays.toString(cechy));
	}

	public TypCechy[] getCechy() {
		return cechy;
	}

}
