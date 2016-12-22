# BOTNET

*A botnet showcase*

*Coursework in Computer Security 2015/2016*

Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.


## Build
The app building is provided by Apache Maven. To build the app you need to run

    $app> mvn clean package

If you want to skip tests:

    $app> mvn clean package -P skip-tests

If you want to build with code optimization:

    $app> mvn clean package -P optimize


## Usage
The app can be run both with and without Apache Maven.


### Usage with Apache Maven
To run the app with Apache Maven, you need to run

    $app>mvn exec:java -Dargs="YOUR ARGUMENTS HERE"

For example, to print the app version, you need to run

    $app>mvn exec:java -Dargs="--version"

Running the app this way could be useful during development,
because it is repackaged at every execution.


### Usage without Apache Maven    
To run the app without Apache Maven, you need to run

    $>java -jar path/to/botnet-1.0-SNAPSHOT-jar-with-dependencies.jar YOUR ARGUMENTS HERE

For example, to print the app version, you need to run

    $>java -jar path/to/botnet-1.0-SNAPSHOT-jar-with-dependencies.jar --version


## Authors
Giacomo Marciani, [gmarciani@acm.org](mailto:gmarciani@acm.org)

Michele Porretta, [mporretta@acm.org](mailto:mporretta@acm.org).


## References
Giacomo Marciani and Michele Porretta. 2017. *A Botnet showcase*. Courseworks in Computer Security. University of Rome Tor Vergata, Italy [Read here](https://gmarciani.com)


## License
The project is released under the [MIT License](https://opensource.org/licenses/MIT).
