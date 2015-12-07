package library.walidator;

import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;

public class Walidacja {

	private BooleanProperty prawidlowy = new ReadOnlyBooleanWrapper();
	private ValidationSupport validationSupport;

	public Walidacja() {
		validationSupport = new ValidationSupport();
		validationSupport.validationResultProperty().addListener((o, ov, nv) -> {
			prawidlowy.set(!(nv.getMessages().size() == 0));
		});
	}
	
	/*dodaje walidator do kontrolki ktora musi byc wypelniona*/
	public void dodaj(Control c) {
		dodaj(c, TypWalidacji.NIE_PUSTY);
	}
	
	/*dodaje walidator do kontrolki ktora musi byc wypelniona*/
	public void dodaj(Control c, TypWalidacji typWalidacji) {
		/*sprawdzenie czy wartosc pola tekstowego jest null*/
		if (c instanceof TextInputControl) {
			if (((TextInputControl) c).getText() == null) {
				((TextInputControl) c).setText("");
			}
		}
		/*przypisanie cech domyslnych*/
		ustawCechy(c, typWalidacji.getCechy());
		
		switch (typWalidacji) {
		
		case NIE_PUSTY:
			WalidatoryFactory.addNotEmpty(c, validationSupport);
			break;
			
		case INTEGER:
			WalidatoryFactory.addNotEmptyInteger(c, validationSupport);
			c.getProperties().put(CechaKey.KEY_MIN, Integer.MIN_VALUE);
			c.getProperties().put(CechaKey.KEY_MAX, Integer.MAX_VALUE);
			break;
			
		case LONG:
			WalidatoryFactory.addNotEmptyLong(c, validationSupport);
			c.getProperties().put(CechaKey.KEY_MIN, Long.MIN_VALUE);
			c.getProperties().put(CechaKey.KEY_MAX, Long.MAX_VALUE);
			break;
			
		case DOUBLE:
			WalidatoryFactory.addNotEmptyDouble(c, validationSupport);
			c.getProperties().put(CechaKey.KEY_PRECYZJA_SKALA, "15,4");
			break;
			
		case REGEX:
			WalidatoryFactory.addNotEmptyRegEx(c, validationSupport);
			break;
			
		case TEXT_MIN_MAX:
			WalidatoryFactory.addTextMinMax(c, validationSupport);
			break;
			
		case LISTA:
			WalidatoryFactory.addList(c, validationSupport);
			c.getProperties().put(CechaKey.KEY_KOMUNIKAT, "Ten element nie występuje...");
			break;
			
		case INDEKS:
			WalidatoryFactory.addNotEmptyIndeks(c, validationSupport);
			c.getProperties().put(CechaKey.KEY_MAX_ZNAKOW, 30);
			c.getProperties().put(CechaKey.KEY_DUZE_LITERY, true);
			break;
			
		case TEST_REGEX:
			WalidatoryFactory.addNotEmptyRegExTest(c, validationSupport);
			c.getProperties().put(CechaKey.KEY_KOMUNIKAT, "Wprowadź konstrukcję wyrażenia regularnego...");
			break;
			
		case LOGICZNY:
			WalidatoryFactory.addNotEmptyLogiczny(c, validationSupport);
			c.getProperties().put(CechaKey.KEY_KOMUNIKAT, "Uzupełnij wszystkie pola...");
			break;
			
		default:
			break;
		}
		
	}

	/* usuwanie walidatora controlsfx */
	public void usun(Control c) {
		validationSupport.registerValidator(c, Validator.combine());
		ValidationSupport.setRequired(c, false);
	}
	
	public void usunWszystkie(){
		validationSupport.getRegisteredControls().forEach(v -> {
			usun(v);
		});
	}

	/*dodanie cech do kontrolek na podstawie szablonu*/
	private void ustawCechy(Control c, TypCechy[] typCechy) {
		for (TypCechy v : typCechy) {
			c.getProperties().put(v.getKey(), v.getWartosc());
		}
	}

	public void ustawCeche(Control c, TypCechy typCechy, Object wartosc, boolean... wymusWalidator) {
		c.getProperties().put(typCechy.getKey(), wartosc);
		boolean wymus = wymusWalidator.length > 0 ? wymusWalidator[0] : true;
		if(wymus){
			wymusWalidator(c);
		}
	}

	public void setAktywny(boolean value){
		validationSupport.setErrorDecorationEnabled(value);
	}
	
	public void setError(Control c, boolean value, String komunikat){
		c.getProperties().put(CechaKey.KEY_KOMUNIKAT, komunikat == null ? "Brak elementu w bazie danych" : komunikat);
		if(value){
			validationSupport.getValidationDecorator().removeDecorations(c);//fix
			validationSupport.getValidationDecorator().applyRequiredDecoration(c);//fix
			validationSupport.getValidationDecorator().applyValidationDecoration(ValidationMessage.error(c, komunikat == null ? "Brak elementu w bazie danych" : komunikat));
		}else{
			validationSupport.getValidationDecorator().removeDecorations(c);
			validationSupport.getValidationDecorator().applyRequiredDecoration(c);
		}

		boolean size = false;
		if(validationSupport.validationResultProperty().getValue() != null){
			size = validationSupport.validationResultProperty().get().getMessages().size() > 0; 
		}
		prawidlowy.set(size || value);//fix
	}
	
	private void wymusWalidator(Control c) {
		if (c instanceof TextInputControl) {
			String tempText = ((TextInputControl) c).getText();
			
			if(!tempText.isEmpty()){
				((TextInputControl) c).clear();
				((TextInputControl) c).setText(tempText);
			}
		}
	}

	public void usunCeche(Control c, TypCechy typCechy) {
		c.getProperties().remove(typCechy.getKey());
	}

	public void ustawCechyDomyslne(Control c){
		String key = (String)c.getProperties().get(WalidatoryFactory.KEY_WALIDATOR);
		
		switch (key) {
		
		case WalidatoryFactory.VALUE_INTEGER:

			c.getProperties().put(CechaKey.KEY_MIN, Integer.MIN_VALUE);
			c.getProperties().put(CechaKey.KEY_MAX, Integer.MAX_VALUE);
			wymusWalidator(c);
			break;
			
		case WalidatoryFactory.VALUE_LONG:
			c.getProperties().put(CechaKey.KEY_MIN, Long.MIN_VALUE);
			c.getProperties().put(CechaKey.KEY_MAX, Long.MAX_VALUE);
			wymusWalidator(c);
			break;
			
		case WalidatoryFactory.VALUE_DOUBLE:
			c.getProperties().put(CechaKey.KEY_PRECYZJA_SKALA, "15,4");
			wymusWalidator(c);
			break;
		}
		
	}
	
	public void setWymagany(Control c, boolean wartosc) {
		ValidationSupport.setRequired(c, wartosc);
	}

	public BooleanProperty prawidlowyProperty() {
		return prawidlowy;
	}
}
