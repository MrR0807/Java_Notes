      public class PatchField<T> {

          private boolean isSet;
          private T value;

          public PatchField(T value) {
              this.isSet = true;
              this.value = value;
          }

          public PatchField(boolean isSet, T value) {
              this.isSet = isSet;
              this.value = value;
          }

          public static <T> PatchField<T> empty() {
              return new PatchField<>(false, null);
          }

          public boolean isSet() {
              return this.isSet;
          }

          public T getValue() {
              return this.value;
          }

          public void ifSet(Consumer<? super T> action) {
              if (this.isSet) {
                  action.accept(this.value);
              }
          }
      }


      /**
       * Value extractor is required for Javax validator. It enables to annotate constrains on container value.
       * For example: PatchField<@Min(0) @Max(100) BigDecimal>. Without value extractor validator cannot know how to extract a value from given
       * container and apply constrains.
       * <p>
       * Container elements link: https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#container-element-constraints
       * Value Extractor link: https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#_implementing_a_code_valueextractor_code
       */
      public class PatchFieldValueExtractor implements ValueExtractor<PatchField<@ExtractedValue ?>> {

          @Override
          public void extractValues(PatchField<?> originalValue, ValueReceiver receiver) {
              receiver.value(null, originalValue.getValue());
          }
      }



      /**
       * Created PatchFieldValueExtractor needs to be register with javax validator as
       * Hibernate Validator does not detect automatically the value extractors in the classpath.
       * It can be done by calling Configuration#addValueExtractor(ValueExtractor<?>)
       * <p>
       *
       * @see PatchFieldValueExtractor
       * @see PatchField
       * Registering link: https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-valueextraction-registeringvalueextractor
       */
      @Component
      public class CustomLocalValidatorFactoryBean extends LocalValidatorFactoryBean {

          @Override
          protected void postProcessConfiguration(Configuration<?> configuration) {
              configuration.addValueExtractor(new PatchFieldValueExtractor());
              super.postProcessConfiguration(configuration);
          }
      }
