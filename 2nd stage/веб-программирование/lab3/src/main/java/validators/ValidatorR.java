package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

@FacesValidator("validatorR")
public class ValidatorR implements Validator {

    private static final String NUMBER_PATTERN = "^(-)?[0-9]+(\\.[0-9]+)?$";

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if (o == null) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "The R field cannot be empty!"));
        }

        String input = o.toString();

        input = input.replace(",", ".");

        if (!Pattern.matches(NUMBER_PATTERN, input)) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "The value of R must be a number!"));
        }

        BigDecimal r = new BigDecimal(input);

        BigDecimal minR = new BigDecimal("1.25");
        BigDecimal maxR = new BigDecimal("3.75");
        BigDecimal step = new BigDecimal("0.25");

        BigDecimal currentValue = minR;
        boolean isValidR = false;
        while (currentValue.compareTo(maxR) <= 0) {
            if (currentValue.compareTo(r) == 0) {
                isValidR = true;
                break;
            }
            currentValue = currentValue.add(step);
        }

        if (!isValidR) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "The value of R must be one of the numbers in the range from 1,25 to 3,75 with step 0,25"));
        }
    }
}
