## Swagger UI integration

notes
1. The jakarta validation annotations only seem to be respected on objects. Use the swagger @Parameter annotation for parameters.
2. Swagger will not recognize an endpoint as secured by default. @SecurityScheme is required to define security at the application level and @SecurityRequirement is required to define security at the endpoint level.
3. This was tested only with basic auth.
4. It is unclear how server detection will work live.