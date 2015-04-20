package com.example.helloworld;

import com.example.helloworld.core.Template;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 Creating A Configuration Class

 Each Dropwizard application has its own subclass of the Configuration
 class which specifies environment-specific parameters. These
 parameters are specified in a YAML configuration file which is deserialized
 to an instance of your application’s configuration class and validated.

 The application we’ll be building is a high-performance Hello World service,
 and one of our requirements is that we need to be able to vary how it says
 hello from environment to environment. We’ll need to specify at least two
 things to begin with: a template for saying hello and a default name to use
 in case the user doesn’t specify their name.
 */
public class HelloWorldConfiguration extends Configuration {
    /**
     * When this class is deserialized from the YAML file, it will
     * pull two root-level fields from the YAML object: template,
     * the template for our Hello World saying, and defaultName, the
     * default name to use. Both template and defaultName are
     * annotated with @NotEmpty, so if the YAML configuration file
     * has blank values for either or is missing template entirely
     * an informative exception will be thrown and your application won’t start.
     */

    @NotEmpty
    private String template;
    
    @NotEmpty
    private String defaultName;

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory databaseConfiguration = new DataSourceFactory();

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }


    public Template buildTemplate() {
        return new Template(template, defaultName);
    }

    public DataSourceFactory getDatabaseConfiguration() {
        return databaseConfiguration;
    }

    public void setDatabaseConfiguration(DataSourceFactory databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }
}
