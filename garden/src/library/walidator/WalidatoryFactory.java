package library.walidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;

/**
 * @author PGurzynski
 *
 */
public class WalidatoryFactory {

	public static final String KEY_WALIDATOR = "$walidator.typ$";
	public static final String VALUE_LONG = "long";    
	public static final String VALUE_INTEGER = "integer";
	public static final String VALUE_DOUBLE = "double";
	public static final String VALUE_REGEX = "regex";
	public static final String VALUE_NIEPUSTY = "niepusty";
	public static final String VALUE_TXT_MIN_MAX = "txt.min.max";
	public static final String VALUE_LISTA = "lista";
	public static final String VALUE_INDEKS = "indeks";
	public static final String VALUE_LOGICZNY = "logiczny";
	
	private enum TypZdarzenia {
		FILTER,
		HANDLER
	}
	
	private static Map<Control, List<Zdarzenie>> mapEvents = new HashMap<Control, List<Zdarzenie>>();
	private static Map<Control, List<MapChangeListener<Object, Object>>> mapListeners = new HashMap<Control, List<MapChangeListener<Object, Object>>>();
	private static Map<Control, List<ChangeListener<Boolean>>> mapFocuseListeners = new HashMap<Control, List<ChangeListener<Boolean>>>();

	public static void addNotEmpty(Control c, ValidationSupport validationSupport){
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_NIEPUSTY);

		/*dodanie evento i listenerow do kontrolek tekstowych*/
		if(c instanceof TextInputControl){
			/*usuwanie jesli sa zdarzenia*/
			removeEvents(c);
			/*dodanie zdarzen*/
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED, Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEventWielkoscZnakow());                    
			/*usuwanie sluchaczy*/
			removeAllListeners(c);
			/*dodanie sluchaczy*/
			addListener(c, Utils.listenerZmianaCech((TextInputControl) c));
		}
		
		/*dodanie walidatorow*/
		validationSupport.registerValidator(c, true, Validator.createEmptyValidator(getKomunikat(c)));
//		validationSupport.registerValidator(c, true, (Control cl, String value) -> 
//			ValidationResult.fromErrorIf(cl, getKomunikat(c), testNotEmpty(cl, value))
//			);
	}
	
	public static void addList(Control c, ValidationSupport validationSupport){
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_LISTA);
		
		/*dodanie evento i listenerow do kontrolek tekstowych*/
		if(c instanceof TextInputControl){
			/*usuwanie jesli sa zdarzenia*/
			removeEvents(c);
			/*dodanie zdarzen*/
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED, Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEventWielkoscZnakow());                    
			/*usuwanie sluchaczy*/
			removeAllListeners(c);
			removeAllFocuseListeners(c);
			/*dodanie sluchaczy*/
			addListener(c, Utils.listenerZmianaCech((TextInputControl) c));
		}
		
		/*dodanie walidatorow*/
		validationSupport.registerValidator(c, true, (Control cl, String value) -> 
			ValidationResult.fromErrorIf(cl, getKomunikat(c), testLista(cl, value))
			);
	}
	
	/*mteoda sprawdza wystepowanie elemntu na liscie*/
	@SuppressWarnings("unchecked")
	private static boolean testLista(Control c, String value) {
        if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
            return false;
      }

//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		Collection<String> lista = (Collection<String>)c.getProperties().get(CechaKey.KEY_LISTA);
		boolean nieLista = (boolean)c.getProperties().get(CechaKey.KEY_NIE_LISTA);
		
		if(lista == null){
			return true;
		}
		
		return nieLista ? lista.contains(value) : !lista.contains(value);
	}

	public static void addTextMinMax(Control c, ValidationSupport validationSupport){
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_TXT_MIN_MAX);
		
		/*dodanie evento i listenerow do kontrolek tekstowych*/
		if(c instanceof TextInputControl){
			/*usuwanie jesli sa zdarzenia*/
			removeEvents(c);
			/*dodanie zdarzen*/
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED, Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEventWielkoscZnakow());                    
			/*usuwanie sluchaczy*/
			removeAllListeners(c);
			/*dodanie sluchaczy*/
			addListener(c, Utils.listenerZmianaCech((TextInputControl) c));
		}
		
		/*dodanie walidatorow*/
		validationSupport.registerValidator(c, true, (Control cl, String value) -> 
			ValidationResult.fromErrorIf(cl, getKomunikat(c), testMinMax(cl, value)
		));
	}
	

	private static boolean testMinMax(Control c, String value) {
//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		int max = (int)c.getProperties().get(CechaKey.KEY_MAX_ZNAKOW);
		int min = (int)c.getProperties().get(CechaKey.KEY_MIN_ZNAKOW);
		
		if (((TextInputControl) c).getText() == null) {
			((TextInputControl) c).setText("");
		}
		if (value == null) {
			value = "";
		}

		/*test na pole puste*/
		if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
			return false;
		}
		
		if(min > max){
			return true;
		}
		
		int length = ((TextInputControl)c).getText().length();

		return !(length >= min && length <= max);
	}


	public static void addNotEmptyLong(Control c, ValidationSupport validationSupport) {
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_LONG);
		
		if(c instanceof TextField){
			/*usuwanie jesli sa zdarzenia*/
			removeEvents(c);
			/*dodanie zdarzen*/
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED, Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEvenIntFilter()); 
			/*usuwanie sluchaczy*/
			removeAllListeners(c);
			/*dodanie sluchaczy*/
			addListener(c, Utils.listenerZmianaCech((TextField) c));
		}

		/*rejestracja walidatora*/
		validationSupport.registerValidator(c, true, (Control cl, String value) -> 
			ValidationResult.fromErrorIf(cl, toolTipZakres(cl), testLong(cl, value)
		));
	}
	
	/*mteoda sprawdza zakresy dla Longa*/
	private static boolean testLong(Control c, String value) {
//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		long max = (long)c.getProperties().get(CechaKey.KEY_MAX);
		long min = (long)c.getProperties().get(CechaKey.KEY_MIN);
		
		if (((TextInputControl) c).getText() == null) {
			((TextInputControl) c).setText("");
		}
		if (value == null) {
			value = "";
		}

		/*test na pole puste*/
		if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
			return false;
		}

		try {
			long i = Long.parseLong(value);
			return !((i >= min) && (i <= max));
		} catch (Exception e) {
			return true;
		}
	}

	
	public static void addNotEmptyInteger(Control c, ValidationSupport validationSupport) {
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_INTEGER);
		
		if(c instanceof TextField){
			/*usuwanie jesli sa zdarzenia*/
			removeEvents(c);
			/*dodanie zdarzen*/
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED, Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEvenIntFilter()); 
			/*usuwanie sluchaczy*/
			removeAllListeners(c);
			/*dodanie sluchaczy*/
			addListener(c, Utils.listenerZmianaCech((TextField) c));
		}
		
		/*rejestracja walidatora*/
		validationSupport.registerValidator(c, true, (Control cl, String value) -> 
		ValidationResult.fromErrorIf(cl, toolTipZakres(cl), testInt(cl, value)));
	}
	
	/*mteoda sprawdza zakresy dla Integera*/
	private static boolean testInt(Control c, String value) {
//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		int max = (int)c.getProperties().get(CechaKey.KEY_MAX);
		int min = (int)c.getProperties().get(CechaKey.KEY_MIN);
		
		if (((TextInputControl) c).getText() == null) {
			((TextInputControl) c).setText("");
		}
		if (value == null) {
			value = "";
		}
		
		/*test na pole puste*/
		if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
			return false;
		}
		
		try {
			int i = Integer.parseInt(value);
			return !((i >= min) && (i <= max));
		} catch (Exception e) {
			return true;
		}
	}
	
	
	public static void addNotEmptyDouble(Control c, ValidationSupport validationSupport) {
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_DOUBLE);

		if(c instanceof TextField){
			/*usuwanie jesli sa zdarzenia*/
			removeEvents(c);
			
//			((TextInputControl) c).setTextFormatter(getNumberFormatter());
			/*dodanie zdarzen*/
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED, Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEvenDoubleFilter()); 
//			/*usuwanie sluchaczy*/
			removeAllListeners(c);
//			removeAllFocuseListeners(c);
			/* dodanie sluchaczy */
			addListener(c, Utils.listenerZmianaCech((TextField) c));
//			addFocusListener(c, Utils.formaterDouble((TextInputControl)c));
		}
		
		
		/*rejestracja walidatora*/
		validationSupport.registerValidator(c, true, (Control cl, String value) -> 
			ValidationResult.fromErrorIf(cl, toolTipZakres(cl), testDouble(cl, value)));
	}
	
//	public static void addNotEmptyDouble(Control c, ValidationSupport validationSupport) {
//		/*oznaczenie walidowanej kontrolki*/
//		c.getProperties().put(KEY_WALIDATOR, VALUE_DOUBLE);
//		
//		if(c instanceof TextField){
//			/*usuwanie jesli sa zdarzenia*/
//			removeEvents(c);
//			/*dodanie zdarzen*/
//			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED, Utils.keyEventMaxZnakow());
//			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEvenDoubleFilter()); 
//			/*usuwanie sluchaczy*/
//			removeAllListeners(c);
//			removeAllFocuseListeners(c);
//			/* dodanie sluchaczy */
//			addListener(c, Utils.listenerZmianaCech((TextField) c));
//			addFocusListener(c, Utils.formaterDouble((TextInputControl)c));
//		}
//		
//		/*rejestracja walidatora*/
//		validationSupport.registerValidator(c, true, (Control cl, String value) -> 
//		ValidationResult.fromErrorIf(cl, toolTipZakres(cl), testDouble(cl, value)));
//	}
	
	/*metoda sprawdza zakres dla double*/
	private static boolean testDouble(Control c, String value) {
//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		double max = Double.parseDouble(String.valueOf(c.getProperties().get(CechaKey.KEY_MAX)));
		double min = Double.parseDouble(String.valueOf(c.getProperties().get(CechaKey.KEY_MIN)));;
//		double max = (double)c.getProperties().get(CechaKey.KEY_MAX);
//		double min = (double)c.getProperties().get(CechaKey.KEY_MIN);
		
		if (((TextInputControl) c).getText() == null) {
			((TextInputControl) c).setText("");
		}
		if (value == null) {
			value = "";
		}
		
		/*test na pole puste*/
		if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
			return false;
		}
		
		try {
			double i = Double.parseDouble(value.replace(",", "."));
//			double i = Double.parseDouble(value);
			return !((i >= min) && (i <= max));
		} catch (Exception e) {
			return true;
		}
	}
	
	/*metoda dodaje dymek dla zakresu MIN MAX*/
	private static String toolTipZakres(Control c){
		Number max = (Number)c.getProperties().get(CechaKey.KEY_MAX);
		Number min = (Number)c.getProperties().get(CechaKey.KEY_MIN);
		String result = "Zakres ["+min+", "+max+"]";
		c.getProperties().put(CechaKey.KEY_KOMUNIKAT, result);
		return result;
	}
	
	public static void addNotEmptyRegEx(Control c, ValidationSupport validationSupport) {
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_REGEX);
		
		if (c instanceof TextField) {
			/* usuwanie jesli sa zdarzenia */
			removeEvents(c);
			/* dodanie zdarzen */
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED,Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEventWielkoscZnakow());  
			/* usuwanie sluchaczy */
			removeAllListeners(c);
			/* dodanie sluchaczy */
			addListener(c, Utils.listenerZmianaCech((TextField) c));
		}

		/* rejestracja walidatora */
		validationSupport.registerValidator(c, true,
				(Control cl, String value) -> ValidationResult.fromErrorIf(cl,getKomunikat(c), testRegEx(cl, value)));
	}
	
	/* metoda testuje pole na porawnosc wyrazenia regularnego */
	private static boolean testRegEx(Control c, String value) {
//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		String regex = (String) c.getProperties().get(CechaKey.KEY_PATTERN);
		Pattern pattern = Pattern.compile(regex);
		
		if (((TextInputControl) c).getText() == null) {
			((TextInputControl) c).setText("");
		}
		if (value == null) {
			value = "";
		}
		
		/*test na pole puste*/
		if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
			return false;
		}
		
		return !(pattern.matcher(value == null  ? "" : value).matches());
	}
	
	public static void addNotEmptyRegExTest(Control c, ValidationSupport validationSupport) {
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_REGEX);
		
		if (c instanceof TextField) {
			/* usuwanie jesli sa zdarzenia */
			removeEvents(c);
			/* dodanie zdarzen */
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED,Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEventWielkoscZnakow());  
			/* usuwanie sluchaczy */
			removeAllListeners(c);
			/* dodanie sluchaczy */
			addListener(c, Utils.listenerZmianaCech((TextField) c));
		}
		
		/* rejestracja walidatora */
		validationSupport.registerValidator(c, true, (Control cl, String value) -> ValidationResult.fromErrorIf(cl,getKomunikat(c), !testRegExTest(cl, value)));
	}
	
	/* metoda testuje pole na porawnosc wyrazenia regularnego */
	private static boolean testRegExTest(Control c, String value) {
//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		if (value == null) {
			return false;
		}
		
		if (value.isEmpty()) {
			return false;
		}
		
		try {
			Pattern.compile(value);
			return true;
		} catch (PatternSyntaxException e) {
			return false;
		}
	}
	
	public static void addNotEmptyIndeks(Control c, ValidationSupport validationSupport) {
		/*oznaczenie walidowanej kontrolki*/
		c.getProperties().put(KEY_WALIDATOR, VALUE_INDEKS);
		
		if (c instanceof TextField) {
			/* usuwanie jesli sa zdarzenia */
			removeEvents(c);
			/* dodanie zdarzen */
			addEvent(c, TypZdarzenia.FILTER, KeyEvent.KEY_TYPED,Utils.keyEventMaxZnakow());
			addEvent(c, TypZdarzenia.HANDLER, KeyEvent.KEY_TYPED, Utils.keyEventWielkoscZnakow());  
			/*usuwanie sluchaczy*/
			removeAllListeners(c);
			removeAllFocuseListeners(c);
			/* dodanie sluchaczy */
			addListener(c, Utils.listenerZmianaCech((TextField) c));
			addFocusListener(c, Utils.formaterDouble((TextInputControl)c));
		}
		
		/* rejestracja walidatora */
		validationSupport.registerValidator(c, true, (Control cl, String value) -> ValidationResult.fromErrorIf(cl,getKomunikat(c), testIndeks(cl, value)));
	}
	
	/* metoda testuje pole na porawnosc wyrazenia regularnego */
	private static boolean testIndeks(Control c, String value) {
//		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
//		if(error){
//			return true;
//		}
		/*test na pole puste*/
		if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
			return false;
		}
		
		long idGrupaAtrybutow = (long)c.getProperties().get(CechaKey.KEY_ID);
		if(idGrupaAtrybutow == -1 || idGrupaAtrybutow == 0 ){
			return true;
		}
		
		String regex = (String) c.getProperties().get(CechaKey.KEY_PATTERN);
		Pattern pattern = Pattern.compile(regex);
		
		if (((TextInputControl) c).getText() == null) {
			((TextInputControl) c).setText("");
		}
		if (value == null) {
			value = "";
		}
		
		/*test na pole puste*/
		if((boolean) c.getProperties().get(CechaKey.KEY_POLE_PUSTE) && value.isEmpty()){
			return false;
		}
		
		return !(pattern.matcher(value == null  ? "" : value).matches());
	}
	
	//TODO - logiczny
	public static void addNotEmptyLogiczny(Control c, ValidationSupport validationSupport) {
		/* oznaczenie walidowanej kontrolki */
		c.getProperties().put(KEY_WALIDATOR, VALUE_LOGICZNY);
		/* rejestracja walidatora */
		validationSupport.registerValidator(c, true, (Control cl, Object value) -> ValidationResult.fromErrorIf(cl, getKomunikat(c), testLogiczny(cl, value)));
	}
	
	/* metoda testuje pole na porawnosc wyrazenia regularnego */
	private static boolean testLogiczny(Control c, Object value) {
		boolean error = (boolean)c.getProperties().get(CechaKey.KEY_FORCE_ERROR);
		return error;
	}
	
	/*metoda dodaje Event do mapy*/
	private static void addEvent(Control c, TypZdarzenia typZdarzenia, EventType<KeyEvent> type, EventHandler<KeyEvent> handler){
		if(!mapEvents.containsKey(c)){
			mapEvents.put(c, new ArrayList<Zdarzenie>());
		}
		
		mapEvents.get(c).add(new Zdarzenie(typZdarzenia, type, handler));

		switch (typZdarzenia) {
		case FILTER:
			c.addEventFilter(type, handler);
			break;
		case HANDLER:
			c.addEventHandler(type, handler);
			break;
		}
	}
	
	/*metoda usuwa wszystki dodane eventy*/
	private static void removeEvents(Control c){
		List<Zdarzenie> list;
		/*usuwanie event filter*/
		if(mapEvents.containsKey(c)){
			list = mapEvents.get(c);
			list.forEach(v->{
				switch (v.getTypZdarzenia()) {
				case FILTER:
					c.removeEventFilter(v.getEventType(), v.getHandler());
					break;
				case HANDLER:
					c.removeEventHandler(v.getEventType(), v.getHandler());
					break;
				}
			});
			list.clear();
			mapEvents.remove(c);
		}
	}
	
	/*dodanie listenera do kontroli*/
	private static void addListener(Control c, MapChangeListener<Object, Object> listener){
		
		if(!mapListeners.containsKey(c)){
			mapListeners.put(c, new ArrayList<MapChangeListener<Object, Object>>());
		}
		
		mapListeners.get(c).add(listener);
		c.getProperties().addListener(listener);
	}
	
	/*metoda usuwa wszystkie listenery*/
	private static void removeAllListeners(Control c){
		List<MapChangeListener<Object, Object>> list;

		if(mapListeners.containsKey(c)){
			list = mapListeners.get(c);
			list.forEach(v->{
				c.getProperties().removeListener(v);
			});
			list.clear();
			mapListeners.remove(c);
		}
	}
	
	/*dodanie focuse listenera*/
	private static void addFocusListener(Control c, ChangeListener<Boolean> listener){
		
		if(!mapFocuseListeners.containsKey(c)){
			mapFocuseListeners.put(c, new ArrayList<ChangeListener<Boolean>>());
		}
		
		mapFocuseListeners.get(c).add(listener);
		c.focusedProperty().addListener(listener);
	}
	
	/*metoda usuwa wszystkie listenery dla focusa*/
	private static void removeAllFocuseListeners(Control c){
		List<ChangeListener<Boolean>> list;
		
		if(mapFocuseListeners.containsKey(c)){
			list = mapFocuseListeners.get(c);
			list.forEach(v->{
				c.focusedProperty().removeListener(v);
			});
			list.clear();
			mapFocuseListeners.remove(c);
		}
	}
	
	/*metoda odczytuje tresc dla dymku walidatora*/
	private static String getKomunikat(Control c) {
		return c.getProperties().get(CechaKey.KEY_KOMUNIKAT).toString();
	}
	
	private static class Zdarzenie {
		private TypZdarzenia typZdarzenia;
		private EventType<KeyEvent> eventType;
		private EventHandler<KeyEvent> handler;
		
		public Zdarzenie(TypZdarzenia typZdarzenia, EventType<KeyEvent> eventType, EventHandler<KeyEvent> handler) {
			super();
			this.typZdarzenia = typZdarzenia;
			this.eventType = eventType;
			this.handler = handler;
		}

		public TypZdarzenia getTypZdarzenia() {
			return typZdarzenia;
		}

		public EventType<KeyEvent> getEventType() {
			return eventType;
		}

		public EventHandler<KeyEvent> getHandler() {
			return handler;
		}

		@Override
		public String toString() {
			return "Zdarzenie [typZdarzenia=" + typZdarzenia + ", eventType="
					+ eventType + ", handler=" + handler + "]";
		}
	}
	
	/*test*/
	public static void pokazNaKonsoli() {
		mapEvents.forEach((k, v) -> {
				System.out.println(k + ", " + v);
		});
		
		System.out.println();
		
		mapListeners.forEach((k, v) -> {
				System.out.println(k + ", " + v);
		});
	}


}
