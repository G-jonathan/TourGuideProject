package tourGuide.beans;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import org.junit.jupiter.api.Test;

class VisitedLocationBeanTest {

    @Test
    public void validateVisitedLocationBeanGetters() {
        PojoClass VisitedLocationBeanPojo = PojoClassFactory.getPojoClass(VisitedLocationBean.class);
        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new GetterTester())
                .build();
        validator.validate(VisitedLocationBeanPojo);
    }
}