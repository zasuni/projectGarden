package library.walidator;

public enum TypCechy {
	 
	KOMUNIKAT(CechaKey.KEY_KOMUNIKAT, "To pole nie może być puste..."),
	MASKA(CechaKey.KEY_MASKA, ""),
	PRECYZJA_SKALA(CechaKey.KEY_PRECYZJA_SKALA,"1,0"),
	MIN_ZNAKOW(CechaKey.KEY_MIN_ZNAKOW, 0),
	MAX_ZNAKOW(CechaKey.KEY_MAX_ZNAKOW, -1),
	MIN(CechaKey.KEY_MIN, 0),
	MAX(CechaKey.KEY_MAX, 1),
	DUZE_LITERY(CechaKey.KEY_DUZE_LITERY, false),
	LITERY(CechaKey.KEY_LITERY, true),
	CYFRY(CechaKey.KEY_CYFRY, true),
	INNE(CechaKey.KEY_INNE, true),
	NIE_LISTA(CechaKey.KEY_NIE_LISTA, false),
	PATTERN(CechaKey.KEY_PATTERN, ""),
	TEMP_PATTERN(CechaKey.KEY_TEMP_PATTERN, ""),
	REPLACE(CechaKey.KEY_REPLACE, ""),
	POLE_PUSTE(CechaKey.KEY_POLE_PUSTE, false),
	LISTA(CechaKey.KEY_LISTA, null),
	ORGIN_VALUE(CechaKey.KEY_ORGIN_VALUE, ""),
	ID(CechaKey.KEY_ID, -1L),
	FORCE_ERROR(CechaKey.KEY_FORCE_ERROR, false);
	
	
	private String key;
	private Object wartosc;

	private TypCechy(String key, Object wartosc) {
		this.key = key;
		this.wartosc = wartosc;
	}

	public Object getWartosc() {
		return wartosc;
	}

	public String getKey() {
		return key;
	}

}
