package tourGuide.beans;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import org.junit.jupiter.api.Test;

class ProviderBeanTest {

    @Test
    public void validateProviderBeanGetters() {
        PojoClass ProviderBeanPojo = PojoClassFactory.getPojoClass(ProviderBean.class);
        Validator validator = ValidatorBuilder.create()
                .with(new GetterMustExistRule())
                .with(new GetterTester())
                .build();
        validator.validate(ProviderBeanPojo);
    }
}