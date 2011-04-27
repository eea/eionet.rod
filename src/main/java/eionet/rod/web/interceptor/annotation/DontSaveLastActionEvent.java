package eionet.rod.web.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When method is annotated with this annotation {@link ActionEventInterceptor}
 * will not save in session this event as last event.
 *
 * @author gerasvad
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DontSaveLastActionEvent {

}
