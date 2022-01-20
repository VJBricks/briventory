# Briventory

## Contribution

### Compile and run

As Play!Framework has not published the version 2.9, the lack of exported modules makes the run fail.

You should run the app using the following VM parameter :

`--add-exports java.base/sun.net.www.protocol.file=ALL-UNNAMED`

For example, this can be achieved in Intellij by setting this option to the `sbt-shell` launch command. Go to _Preferences_, _Build, Execution, Deployment_, _Build Tools_, _sbt_ and add it to the _VM parameters_.

When building the Docker image, this parameter can be passed to the JVM, using the `-J` option :

```shell
/app/bin/briventory -Dconfig.file=$CONF_FILE -Dpidfile.path=/var/run/briventory.pid -Djava.awt.headless=true -J--add-exports -Jjava.base/sun.net.www.protocol.file=ALL-UNNAMED
```

> ⚠️ `-J` should be specified after each whitespace. 
