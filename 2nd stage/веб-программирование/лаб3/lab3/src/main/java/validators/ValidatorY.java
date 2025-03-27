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

@FacesValidator("validatorY")
public class ValidatorY implements Validator {
    private static final String NUMBER_PATTERN = "^(-)?[0-9]+(\\.[0-9]+)?$";
    private static final BigDecimal MAX_Y = new BigDecimal("5");
    private static final BigDecimal MIN_Y = new BigDecimal("-3");

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String source = requestParameterMap.get("form:source");
        if (o == null) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "The Y field cannot be empty!"));
        }

        String input = o.toString();

        input = input.replace(",", ".");

        if (!Pattern.matches(NUMBER_PATTERN, input)) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                            "The value of Y must be a number!"));
        }

        if ("button".equals(source)) {
            BigDecimal yValue = new BigDecimal(input);

            if (yValue.compareTo(MIN_Y) <= 0 || yValue.compareTo(MAX_Y) >= 0) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                                String.format("The Y value must be in the range (%s;%s)", MIN_Y, MAX_Y)));
            }
        }
    }
}
