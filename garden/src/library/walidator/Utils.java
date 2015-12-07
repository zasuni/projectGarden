package library.walidator;

import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.NumberStringConverter;

public class Utils {

	/*sluchacz zmiaan cech*/
	public static MapChangeListener<Object, Object> listenerZmianaCech(TextInputControl c){
		return change -> {
			/*wyznaczanie dlugosci znakow poprawka dla doubla*/
			if (change.getKey().equals(CechaKey.KEY_PRECYZJA_SKALA)) {
				poprawkaDlaDouble(c);
			}
			
			/*obliczanie dlugosci znakow dla liczb automatycznie*/
			if(change.getKey().equals(CechaKey.KEY_MAX) || change.getKey().equals(CechaKey.KEY_MIN)){
				setNumberLength(c);
			}
			
			/*obsluga komunikatu dla listy*/
			if(change.getKey().equals(CechaKey.KEY_NIE_LISTA)){
				setKomunikatSlaListy(c);
			}

			/*obsluga dynamiczej zmiany maks znakow*/
			if(c.getProperties().containsKey(CechaKey.KEY_MAX_ZNAKOW) && c.getText() != null){
				if(change.getKey().equals(CechaKey.KEY_MAX_ZNAKOW)){
					int maxZnakow = (int) c.getProperties().get(CechaKey.KEY_MAX_ZNAKOW);
					if(maxZnakow <= c.getText().length() && maxZnakow != -1){
						c.setText(c.getText(0, maxZnakow));
					}
				}
			}
		};
	}

	/*obsluga komunikatu dla listy*/
	private static void setKomunikatSlaListy(TextInputControl c) {
		boolean nieLista = (boolean)c.getProperties().get(CechaKey.KEY_NIE_LISTA);
		if(nieLista){
			c.getProperties().put(CechaKey.KEY_KOMUNIKAT, "Ten element już wystepuje...");
		}else{
			c.getProperties().put(CechaKey.KEY_KOMUNIKAT, "Ten element nie występuje...");
		}
	}

	/* obliczanie dlugosci znakow dla liczb automatycznie */
	private static void setNumberLength(TextInputControl c) {
		Number min = (Number) c.getProperties().get(CechaKey.KEY_MIN);
		Number max = (Number) c.getProperties().get(CechaKey.KEY_MAX);
		if (!(min instanceof Double) || !(max instanceof Double)) {
			int dlugoscMax = String.valueOf(min).length();
			if (String.valueOf(max).length() > dlugoscMax) {
				dlugoscMax = String.valueOf(max).length();
			}
			c.getProperties().put(CechaKey.KEY_MAX_ZNAKOW, dlugoscMax);
		}
	}
	
	/*wyznaczenie zakresu dla liczb dziesietnych*/
	private static void poprawkaDlaDouble(TextInputControl c){
		String value = (String) c.getProperties().get(CechaKey.KEY_PRECYZJA_SKALA);
		/*parsowanie wyrazenia*/
		int precyzja;
		int skala;
		try {
			String[] split = value.split("[,]");
			precyzja = Integer.parseInt(split[0].trim());
			skala = Integer.parseInt(split[1].trim());
		} catch (Exception e) {
			precyzja = 15;
			skala = 4;
		}
		
		/*wyznaczenie zakresu*/
		StringBuilder liczba = new StringBuilder();
		for (int i = 0; i < precyzja; i++) {
			liczba.append("9");
		}
		liczba.insert(precyzja-skala, ".");

		/*budowanie maski*/
		StringBuilder maska = new StringBuilder();
		if(skala == 0){
			maska.append("#");
		}else{
			maska.append("#0.");
		}
		for (int i = 0; i < skala; i++) {
			maska.append("0");
		}
		
		c.getProperties().put(CechaKey.KEY_MIN, Double.parseDouble(liczba.toString()) * -1);
		c.getProperties().put(CechaKey.KEY_MAX, Double.parseDouble(liczba.toString()));
		c.setTextFormatter(null);
		c.setTextFormatter(new TextFormatter<>(new NumberStringConverter(maska.toString())));
		c.getProperties().put(CechaKey.KEY_MAX_ZNAKOW, liczba.toString().length() + 1); //+1 dla znaku minus
	}
	

	/* formater dla liczb rzeczywistych */
	public static ChangeListener<Boolean> formaterDouble(TextInputControl c) {
		return (o, ov, nv) -> {
			if(c.getText() == null){
				c.setText("");
			}
			if (ov == true && nv == false && !c.getText().isEmpty()) {
				obslugaPoFokusieDouble(c);
				obslugaPoFokusieIndeks(c);
			}
			if (ov == false && nv == true && !c.getText().isEmpty()) {
				obslugaFokusaIndeks(c);
			}
		};
	}



	private static void obslugaPoFokusieDouble(TextInputControl c) {
		if(c.getProperties().get(WalidatoryFactory.KEY_WALIDATOR) == WalidatoryFactory.VALUE_DOUBLE){
			
			try {
				String maska = (String) c.getProperties().get(CechaKey.KEY_MASKA);
				DecimalFormat format = new DecimalFormat(maska);
				String text = c.getText();
				String result = format.format(Double.parseDouble(text)).toString().replace(",", ".");
				if(text.equals("0")){
					c.setText("0");
				}else{
					c.setText(result);
				}
			} catch (NumberFormatException | NullPointerException e) {
			}
			
		};
	}
	
	private static void obslugaFokusaIndeks(TextInputControl c) {
		if(c.getProperties().get(WalidatoryFactory.KEY_WALIDATOR) == WalidatoryFactory.VALUE_INDEKS){
			try {
				long idGrupaAtrybutow = (long)c.getProperties().get(CechaKey.KEY_ID);
				if(idGrupaAtrybutow > 0 ){
					String result = (String) c.getProperties().get(CechaKey.KEY_ORGIN_VALUE);
					String pattern = (String) c.getProperties().get(CechaKey.KEY_PATTERN);
					String tempPattern = (String) c.getProperties().get(CechaKey.KEY_TEMP_PATTERN);
					
					/*podmiana wyrazenia regularnego*/
					c.getProperties().put(CechaKey.KEY_PATTERN, tempPattern);
					c.getProperties().put(CechaKey.KEY_TEMP_PATTERN, pattern);
					
					c.setText(result);
				}else{
				}
			} catch (NumberFormatException | NullPointerException e) {
			}
		}
		
	}
	
	private static void obslugaPoFokusieIndeks(TextInputControl c) {
		if(c.getProperties().get(WalidatoryFactory.KEY_WALIDATOR) == WalidatoryFactory.VALUE_INDEKS){
			try {
				long idGrupaAtrybutow = (long)c.getProperties().get(CechaKey.KEY_ID);
				if(idGrupaAtrybutow > 0 ){
					String pattern = (String) c.getProperties().get(CechaKey.KEY_PATTERN);
					String tempPattern = (String) c.getProperties().get(CechaKey.KEY_TEMP_PATTERN);
					String replace = (String) c.getProperties().get(CechaKey.KEY_REPLACE);
					
					String value = c.getText();
					String result = value.replaceFirst(pattern, replace);
					
					c.getProperties().put(CechaKey.KEY_ORGIN_VALUE, value);
					/*podmiana wyrazenia regularnego*/
					c.getProperties().put(CechaKey.KEY_PATTERN, tempPattern);
					c.getProperties().put(CechaKey.KEY_TEMP_PATTERN, pattern);
					
					c.setText(result);
				}else{
				}
			} catch (NumberFormatException | NullPointerException e) {
			}
		}
	}
	
	
	/*ograniczenie liczby znakow*/
	public static EventHandler<KeyEvent> keyEventMaxZnakow() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				
				TextInputControl c = (TextInputControl) e.getSource();
				if(c.isEditable()){
					String text = c.getText();
					int selectedLength = c.getSelectedText().length();
					Object keyMaxZnakow = c.getProperties().get(CechaKey.KEY_MAX_ZNAKOW);
					
					/*ograniczenie liczby znakow*/
					if(keyMaxZnakow != null && text != null){
						if(text.length() >= (int) keyMaxZnakow && (int) keyMaxZnakow != -1 ){
							if(!(selectedLength > 0)){
								e.consume(); // nie wywola handlera i mtetod  typu setOn
							}
						}
					}
				}
			}
		};
	}
	
	/*konwersja na duze znaki*/
	public static EventHandler<KeyEvent> keyEventWielkoscZnakow() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				TextInputControl c = (TextInputControl) e.getSource();
				if(c.isEditable()){
					Object keyDuzeLitery = c.getProperties().get(CechaKey.KEY_DUZE_LITERY);
					if(keyDuzeLitery != null){
						if((boolean)keyDuzeLitery && 
								!(e.getCharacter().hashCode() == 3) && //klawisz CRTL+C
								!(e.getCharacter().hashCode() == 24) && //klawisz CRTL+X
								!(e.getCharacter().hashCode() == 22) && //klawisz CRTL+V
								!(e.getCharacter().hashCode() == 8) && //klawisz
								!(e.getCharacter().hashCode() == 127) && //klawisz
								!(e.getCharacter().hashCode() == 13) && //klawisz ENTER
								!(e.getCharacter().hashCode() == 9)){ //klawisz TAB
							if(c.getSelection().getLength() > 0){
								c.deleteText(c.getSelection());
								c.insertText(c.getCaretPosition(), e.getCharacter().toUpperCase());
								e.consume();
							}else{
								c.insertText(c.getCaretPosition(), e.getCharacter().toUpperCase());
								e.consume();
							}
						}
					}
				}
			}
		};
	}
	
	/* filtr dla liczb calkowitych */
	public static EventHandler<KeyEvent> keyEvenIntFilter() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				TextField c = (TextField) e.getSource();
				if(c.isEditable()){
					int selectedLength = c.getSelectedText().length();
					String selectedText = c.getSelectedText();
					
					if (e.getCharacter().matches("[0-9-]")) {
						if (selectedLength > 0 && e.getCharacter().matches("[-]")) {
							if (!selectedText.contains("-") && c.getText().contains("-")) {
								e.consume();//gdy jest zaznaczenie i wcisnieto minus
							}
						} else {
							
							if(c.getText().length() >= 1 && e.getCharacter().matches("[-]") && c.getText().startsWith("-")){
								e.consume();
							}	
							if(c.getText().length() >= 1 && e.getCharacter().matches("[-]") && !c.getText().startsWith("-") && c.getCaretPosition() > 0){
								e.consume();
							}	
						}
					} else {
						e.consume();// gdy znaki sa z poza ograniczenia
					}
				}
			}
		};
	}
	
	/*filtr dla liczb rzeczywistych*/
	public static EventHandler<KeyEvent> keyEvenDoubleFilter() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				TextField c = (TextField) e.getSource();
				if(c.isEditable()){
					int length = c.getSelectedText().length();
					String selectedText = c.getSelectedText();
					
					if (e.getCharacter().matches("[.]") && !c.getText().contains(",")) {
						c.insertText(c.getCaretPosition(), ",");
						e.consume();
					}
					
					if (c.getSelection().getLength() > 0 && e.getCharacter().matches("[.]")) {
						c.deleteText(c.getSelection());
						c.insertText(c.getCaretPosition(), ",");
						e.consume();
					}	
					
					if (e.getCharacter().matches("[0-9,.-]")) {
						
						if (length > 0 && e.getCharacter().matches("[-,.]")) {
							
							if (!selectedText.contains("-") && c.getText().contains("-")) {
								e.consume();
							}
							
						} else {
							if(c.getText().length() >= 1 && e.getCharacter().matches("[-]") && c.getText().startsWith("-")){
								e.consume();
							}
							if(c.getText().length() >= 1 && e.getCharacter().matches("[-]") && !c.getText().startsWith("-") && c.getCaretPosition() > 0){
								e.consume();
							}
							else if (c.getText().contains(",") && (e.getCharacter().matches("[,]") ||  e.getCharacter().matches("[.]"))) {
								e.consume();
							}
						}
					} else {
						e.consume();
					}
					
				}
			}
		};
	}	

	public static String getSymbolNiesformatowany(TextInputControl c){
		return String.valueOf(c.getProperties().get(CechaKey.KEY_ORGIN_VALUE));
	}
		

}
