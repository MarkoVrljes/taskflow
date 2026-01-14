junit-platform-engine-1.10.5.jar:1.10.5]
        at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73) ~[junit-platform-engine-1.10.5.jar:1.10.5]        at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:143) ~[junit-platform-engine-1.10.5.jar:1.10.5]        at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:100) ~[junit-platform-engine-1.10.5.jar:1.10.5]
        at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:35) ~[junit-platform-engine-1.10.5.jar:1.10.5]
        at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57) ~[junit-platform-engine-1.10.5.jar:1.10.5]
        at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:54) ~[junit-platform-engine-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:198) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:169) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:93) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:58) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:141) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:57) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:103) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:85) 
~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.junit.platform.launcher.core.DelegatingLauncher.execute(DelegatingLauncher.java:47) ~[junit-platform-launcher-1.10.5.jar:1.10.5]
        at org.apache.maven.surefire.junitplatform.LazyLauncher.execute(LazyLauncher.java:56) ~[surefire-junit-platform-3.2.5.jar:3.2.5]
        at org.apache.maven.surefire.junitplatform.JUnitPlatformProvider.execute(JUnitPlatformProvider.java:184) ~[surefire-junit-platform-3.2.5.jar:3.2.5]        at org.apache.maven.surefire.junitplatform.JUnitPlatformProvider.invokeAllTests(JUnitPlatformProvider.java:148) ~[surefire-junit-platform-3.2.5.jar:3.2.5]
        at org.apache.maven.surefire.junitplatform.JUnitPlatformProvider.invoke(JUnitPlatformProvider.java:122) ~[surefire-junit-platform-3.2.5.jar:3.2.5] 
        at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:385) ~[surefire-booter-3.2.5.jar:3.2.5]
        at org.apache.maven.surefire.booter.ForkedBooter.execute(ForkedBooter.java:162) ~[surefire-booter-3.2.5.jar:3.2.5]  
        at org.apache.maven.surefire.booter.ForkedBooter.run(ForkedBooter.java:507) ~[surefire-booter-3.2.5.jar:3.2.5]      
        at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:495) ~[surefire-booter-3.2.5.jar:3.2.5]     
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with 
name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Failed to initialize dependency 'flywayInitializer' of LoadTimeWeaverAware bean 'entityManagerFactory': Error creating bean with name 'flywayInitializer' defined in class path resource [org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayConfiguration.class]: Unable to obtain connection from database: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
SQL State  : 08001
Error Code : 0
Message    : Connection to localhost:5432 refused. Check that 
the hostname and port are correct and that the postmaster is accepting TCP/IP connections.   

        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:326) ~[spring-beans-6.1.14.jar:6.1.14]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:205) ~[spring-beans-6.1.14.jar:6.1.14]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:954) ~[spring-context-6.1.14.jar:6.1.14]        
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625) ~[spring-context-6.1.14.jar:6.1.14] 
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754) ~[spring-boot-3.3.5.jar:3.3.5]    
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456) ~[spring-boot-3.3.5.jar:3.3.5]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:335) ~[spring-boot-3.3.5.jar:3.3.5]        
        at org.springframework.boot.test.context.SpringBootContextLoader.lambda$loadContext$3(SpringBootContextLoader.java:137) ~[spring-boot-test-3.3.5.jar:3.3.5]
        at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:58) ~[spring-core-6.1.14.jar:6.1.14]        at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:46) ~[spring-core-6.1.14.jar:6.1.14]        at org.springframework.boot.SpringApplication.withHook(SpringApplication.java:1463) ~[spring-boot-3.3.5.jar:3.3.5]  
        at org.springframework.boot.test.context.SpringBootContextLoader$ContextLoaderHook.run(SpringBootContextLoader.java:553) ~[spring-boot-test-3.3.5.jar:3.3.5]
        at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:137) ~[spring-boot-test-3.3.5.jar:3.3.5] 
        at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:108) ~[spring-boot-test-3.3.5.jar:3.3.5] 
        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:225) ~[spring-test-6.1.14.jar:6.1.14]
        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:152) ~[spring-test-6.1.14.jar:6.1.14]    
        ... 73 common frames omitted
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with 
name 'flywayInitializer' defined in class path resource [org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayConfiguration.class]: 
Unable to obtain connection from database: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.    
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
SQL State  : 08001
Error Code : 0
Message    : Connection to localhost:5432 refused. Check that 
the hostname and port are correct and that the postmaster is accepting TCP/IP connections.   

        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1806) ~[spring-beans-6.1.14.jar:6.1.14]    
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600) ~[spring-beans-6.1.14.jar:6.1.14]       
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.14.jar:6.1.14]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337) ~[spring-beans-6.1.14.jar:6.1.14]        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.14.jar:6.1.14]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335) ~[spring-beans-6.1.14.jar:6.1.14]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.1.14.jar:6.1.14]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:313) ~[spring-beans-6.1.14.jar:6.1.14]
        ... 88 common frames omitted
Caused by: org.flywaydb.core.internal.exception.FlywaySqlException: Unable to obtain connection from database: Connection to localhost:5432 refused. Check 
that the hostname and port are 
correct and that the postmaster is accepting TCP/IP connections.
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
SQL State  : 08001
Error Code : 0
Message    : Connection to localhost:5432 refused. Check that 
the hostname and port are correct and that the postmaster is accepting TCP/IP connections.   

        at org.flywaydb.core.internal.jdbc.JdbcUtils.openConnection(JdbcUtils.java:60) ~[flyway-core-10.10.0.jar:na]        
        at org.flywaydb.core.internal.jdbc.JdbcConnectionFactory.<init>(JdbcConnectionFactory.java:72) ~[flyway-core-10.10.0.jar:na]
        at org.flywaydb.core.FlywayExecutor.execute(FlywayExecutor.java:134) ~[flyway-core-10.10.0.jar:na]
        at org.flywaydb.core.Flyway.migrate(Flyway.java:147) ~[flyway-core-10.10.0.jar:na]   
        at org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer.afterPropertiesSet(FlywayMigrationInitializer.java:66) ~[spring-boot-autoconfigure-3.3.5.jar:3.3.5]   
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1853) ~[spring-beans-6.1.14.jar:6.1.14] 
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1802) ~[spring-beans-6.1.14.jar:6.1.14]    
        ... 95 common frames omitted
Caused by: org.postgresql.util.PSQLException: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections. 
        at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:352) ~[postgresql-42.7.4.jar:42.7.4]
        at org.postgresql.core.ConnectionFactory.openConnection(ConnectionFactory.java:54) ~[postgresql-42.7.4.jar:42.7.4]  
        at org.postgresql.jdbc.PgConnection.<init>(PgConnection.java:273) ~[postgresql-42.7.4.jar:42.7.4]
        at org.postgresql.Driver.makeConnection(Driver.java:446) ~[postgresql-42.7.4.jar:42.7.4]
        at org.postgresql.Driver.connect(Driver.java:298) ~[postgresql-42.7.4.jar:42.7.4]    
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:137) ~[HikariCP-5.1.0.jar:na]        
        at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:360) ~[HikariCP-5.1.0.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:202) ~[HikariCP-5.1.0.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:461) ~[HikariCP-5.1.0.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:550) ~[HikariCP-5.1.0.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:98) ~[HikariCP-5.1.0.jar:na]
        at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:111) ~[HikariCP-5.1.0.jar:na]
        at org.flywaydb.core.internal.jdbc.JdbcUtils.openConnection(JdbcUtils.java:48) ~[flyway-core-10.10.0.jar:na]        
        ... 101 common frames omitted
Caused by: java.net.ConnectException: Connection refused: no further information
        at java.base/sun.nio.ch.Net.pollConnect(Native Method) ~[na:na]
        at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:672) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:542) ~[na:na]
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:597) ~[na:na]       
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327) ~[na:na]     
        at java.base/java.net.Socket.connect(Socket.java:633) 
~[na:na]
        at org.postgresql.core.PGStream.createSocket(PGStream.java:260) ~[postgresql-42.7.4.jar:42.7.4]
        at org.postgresql.core.PGStream.<init>(PGStream.java:121) ~[postgresql-42.7.4.jar:42.7.4]
        at org.postgresql.core.v3.ConnectionFactoryImpl.tryConnect(ConnectionFactoryImpl.java:140) ~[postgresql-42.7.4.jar:42.7.4]
        at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:268) ~[postgresql-42.7.4.jar:42.7.4]
        ... 113 common frames omitted

[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 10.42 s <<< FAILURE! 
-- in com.dusan.taskflow.TaskflowApplicationTests
[ERROR] com.dusan.taskflow.TaskflowApplicationTests.contextLoads -- Time elapsed: 0.006 s <<< ERROR!
java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@30bd39d5 testClass = com.dusan.taskflow.TaskflowApplicationTests, locations = [], classes = [com.dusan.taskflow.TaskflowApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@51bf5add, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@478db956, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@2dc9b0f5, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@7ba8c737, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@16c069df, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@4e928fbf, org.springframework.boot.test.context.SpringBootTestAnnotation@32f9ce28], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]      
        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:180)      
        at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)   
        at org.springframework.test.context.web.ServletTestExecutionListener.setUpRequestContextIfNecessary(ServletTestExecutionListener.java:191)
        at org.springframework.test.context.web.ServletTestExecutionListener.prepareTestInstance(ServletTestExecutionListener.java:130)
        at org.springframework.test.context.TestContextManager.prepareTestInstance(TestContextManager.java:260)
        at org.springframework.test.context.junit.jupiter.SpringExtension.postProcessTestInstance(SpringExtension.java:163) 
        at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
        at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)   
        at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        at java.base/java.util.stream.StreamSpliterators$WrappingSpliterator.forEachRemaining(StreamSpliterators.java:310)  
        at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:735)
        at java.base/java.util.stream.Streams$ConcatSpliterator.forEachRemaining(Streams.java:734)
        at java.base/java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:762)
        at java.base/java.util.Optional.orElseGet(Optional.java:364)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
        at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with 
name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Failed to initialize dependency 'flywayInitializer' of LoadTimeWeaverAware bean 'entityManagerFactory': Error creating bean with name 'flywayInitializer' defined in class path resource [org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayConfiguration.class]: Unable to obtain connection from database: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
SQL State  : 08001
Error Code : 0
Message    : Connection to localhost:5432 refused. Check that 
the hostname and port are correct and that the postmaster is accepting TCP/IP connections.   

        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:326)
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:205)
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:954)
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625)      
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754)    
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:335)        
        at org.springframework.boot.test.context.SpringBootContextLoader.lambda$loadContext$3(SpringBootContextLoader.java:137)
        at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:58)  
        at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:46)  
        at org.springframework.boot.SpringApplication.withHook(SpringApplication.java:1463)  
        at org.springframework.boot.test.context.SpringBootContextLoader$ContextLoaderHook.run(SpringBootContextLoader.java:553)
        at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:137)      
        at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:108)      
        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:225)
        at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:152)      
        ... 17 more
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with 
name 'flywayInitializer' defined in class path resource [org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayConfiguration.class]: 
Unable to obtain connection from database: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.    
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
SQL State  : 08001
Error Code : 0
Message    : Connection to localhost:5432 refused. Check that 
the hostname and port are correct and that the postmaster is accepting TCP/IP connections.   

        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1806)       
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337)   
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335)
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200)
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:313)
        ... 32 more
Caused by: org.flywaydb.core.internal.exception.FlywaySqlException: Unable to obtain connection from database: Connection to localhost:5432 refused. Check 
that the hostname and port are 
correct and that the postmaster is accepting TCP/IP connections.
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
SQL State  : 08001
Error Code : 0
Message    : Connection to localhost:5432 refused. Check that 
the hostname and port are correct and that the postmaster is accepting TCP/IP connections.   

        at org.flywaydb.core.internal.jdbc.JdbcUtils.openConnection(JdbcUtils.java:60)       
        at org.flywaydb.core.internal.jdbc.JdbcConnectionFactory.<init>(JdbcConnectionFactory.java:72)
        at org.flywaydb.core.FlywayExecutor.execute(FlywayExecutor.java:134)
        at org.flywaydb.core.Flyway.migrate(Flyway.java:147)  
        at org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer.afterPropertiesSet(FlywayMigrationInitializer.java:66)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1853)    
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1802)       
        ... 39 more
Caused by: org.postgresql.util.PSQLException: Connection to localhost:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections. 
        at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:352)
        at org.postgresql.core.ConnectionFactory.openConnection(ConnectionFactory.java:54)   
        at org.postgresql.jdbc.PgConnection.<init>(PgConnection.java:273)
        at org.postgresql.Driver.makeConnection(Driver.java:446)
        at org.postgresql.Driver.connect(Driver.java:298)     
        at com.zaxxer.hikari.util.DriverDataSource.getConnection(DriverDataSource.java:137)  
        at com.zaxxer.hikari.pool.PoolBase.newConnection(PoolBase.java:360)
        at com.zaxxer.hikari.pool.PoolBase.newPoolEntry(PoolBase.java:202)
        at com.zaxxer.hikari.pool.HikariPool.createPoolEntry(HikariPool.java:461)
        at com.zaxxer.hikari.pool.HikariPool.checkFailFast(HikariPool.java:550)
        at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:98)
        at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:111)       
        at org.flywaydb.core.internal.jdbc.JdbcUtils.openConnection(JdbcUtils.java:48)       
        ... 45 more
Caused by: java.net.ConnectException: Connection refused: no further information
        at java.base/sun.nio.ch.Net.pollConnect(Native Method)        at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:672)
        at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:542)     
        at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:597)
        at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:327)
        at java.base/java.net.Socket.connect(Socket.java:633) 
        at org.postgresql.core.PGStream.createSocket(PGStream.java:260)
        at org.postgresql.core.PGStream.<init>(PGStream.java:121)
        at org.postgresql.core.v3.ConnectionFactoryImpl.tryConnect(ConnectionFactoryImpl.java:140)
        at org.postgresql.core.v3.ConnectionFactoryImpl.openConnectionImpl(ConnectionFactoryImpl.java:268)
        ... 57 more

[INFO] 
[INFO] Results:
[INFO]
[ERROR] Errors: 
[ERROR]   IntegrationTests » IllegalState Could not find a valid Docker environment. Please see logs and check configuration[ERROR]   TaskflowApplicationTests.contextLoads » IllegalState Failed to load ApplicationContext for [WebMergedContextConfiguration@30bd39d5 testClass = com.dusan.taskflow.TaskflowApplicationTests, locations = [], classes = [com.dusan.taskflow.TaskflowApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@51bf5add, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@478db956, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@2dc9b0f5, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@7ba8c737, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@16c069df, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@4e928fbf, org.springframework.boot.test.context.SpringBootTestAnnotation@32f9ce28], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]       
[INFO]
[ERROR] Tests run: 2, Failures: 0, Errors: 2, Skipped: 0      
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  13.465 s   
[INFO] Finished at: 2026-01-14T12:49:34-05:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal 
org.apache.maven.plugins:maven-surefire-plugin:3.2.5:test (default-test) on project taskflow:
[ERROR]
[ERROR] Please refer to C:\Users\Marko\Desktop\Projects\taskflow\backend\target\surefire-reports for the individual test results.
[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
[ERROR] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the 
-X switch to enable full debug 
logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException       
PS C:\Users\Marko\Desktop\Projects\taskflow\backend>