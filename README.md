# BOTNET

*A botnet showcase*

*Coursework in Computer Security 2015/2016*

Botnets are networks made up of unaware remote-controlled computers, typically instructed for
malicious purposes.
Their first priority is to spread unnoticed, because in this way they may generate huge profits.
They extend infecting computers with malwares — namely bots — that force them to join the network,
unwittingly.
In this work we describe the implementation of a bot, though for botnets with a centralized command
and control layer.
The developed bot is thought as an educational showcase, but it is actually ready to interact with a
real web controller and it is configurable via a convenient web-based interface.


## Build
To build the bot you need to run

    $app> mvn clean package

If you want to build it with code optimization:

    $app> mvn clean package -P optimize

If you want to build it skipping tests:

    $app> mvn clean package -P skip-tests

To build the Command&Control (C&C) server & Web User Interface (WUI) you need to run

    $controller> npm install
    $controller> bower install

## Usage  
To start the bot, you need to run

    $> java -jar path/to/bot-1.0-jar-optimized.jar [YOUR ARGUMENTS HERE]

For example, to print the app version, you need to run:

    $> java -jar path/to/bot-1.0-jar-optimized.jar --version
    
NOTE: when no custom configuration is specified, the bot looks for a file `config.yaml`
in the current working directory. If no such file is found, the bot loads its default
configuration.

To start the Command&Control (C&C), you need to run:

    $controller> node app.js [YOUR ARGUMENTS HERE]

For example, to start the C&C with port 3000 and verbose mode, you need to run:

    $controller> node app.js --port 3000 --verbose

By default, the C&C is bound to port 3000.

The fake landing is located in 

    http://localhost:port/

While the botnet dashboard is located in: 

	http://localhost:port/admin


## Authors
Giacomo Marciani, [gmarciani@acm.org](mailto:gmarciani@acm.org)

Michele Porretta, [mporretta@acm.org](mailto:mporretta@acm.org).


## References
Giacomo Marciani and Michele Porretta. 2017. *A Botnet showcase*. Courseworks in Computer Security. University of Rome Tor Vergata, Italy [Read here](https://gmarciani.com)


## License
The project is released under the [MIT License](https://opensource.org/licenses/MIT).
