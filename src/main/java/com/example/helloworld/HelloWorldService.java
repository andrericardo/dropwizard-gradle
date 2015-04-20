package com.example.helloworld;

import com.example.helloworld.cli.RenderCommand;
import com.example.helloworld.core.Person;
import com.example.helloworld.core.Template;
import com.example.helloworld.db.PersonDAO;
import com.example.helloworld.health.TemplateHealthCheck;
import com.example.helloworld.resources.HelloWorldResource;
import com.example.helloworld.resources.PeopleResource;
import com.example.helloworld.resources.PersonResource;

import com.example.helloworld.resources.ProtectedResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 Combined with your project’s Configuration subclass, its Application subclass
 forms the core of your Dropwizard application. The Application class pulls
 together the various bundles and commands which provide basic functionality.
 (More on that later.) For now, though, our HelloWorldApplication looks like this:
 */
public class HelloWorldService extends Application<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldService().run(args);
    }

    private final HibernateBundle<HelloWorldConfiguration> hibernateBundle =
            new HibernateBundle<HelloWorldConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
                    return configuration.getDatabaseConfiguration();
                }

            };

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
//        bootstrap.setName("hello-world");
        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<HelloWorldConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }


    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws ClassNotFoundException {
        /**
         * When our application starts, we create a new instance of our resource
         * class with the parameters from the configuration file and hand it off
         * to the Environment, which acts like a registry of all the things your
         * application can do.
         */
        final Template template = configuration.buildTemplate();

        final HelloWorldResource resource = new HelloWorldResource(template);
        environment.jersey().register(resource);
        /**
         * add health check
         */
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(template);
        environment.jersey().register(healthCheck);

        /**TODO
         * dummy authenticator, just put whatever username and password "secret"
         */
//        environment.addProvider(new BasicAuthProvider<User>(new ExampleAuthenticator(), "any username, \"secret\""));
//        environment.addResource(new ProtectedResource());

        /**
         * In computer software, a data access object (DAO) is an object that provides
         * an abstract interface to some type of database or other persistence
         * mechanism. By mapping application calls to the persistence layer, DAO
         * provide some specific data operations without exposing details of the database.
         */
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());
        /**
         * People and person resources
         */
        environment.jersey().register(new PeopleResource(dao));
        environment.jersey().register(new PersonResource(dao));


    }
}
