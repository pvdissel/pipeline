package cd.pipeline.runner.cli

import cd.pipeline.runner.dsl.PipelineScriptRunner
import cd.pipeline.runner.internal.ServiceRegistry
import cd.pipeline.util.ServiceLocator
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Paths

class Main {
    static final Logger log = LoggerFactory.getLogger(Main);

    public static void main(String[] args) {
        def exitStatus = 0
        try {
            exitStatus = new Main().run(args)
        } catch (Exception e) {
            System.exit(1)
        }
        System.exit(exitStatus)
    }

    public int run(String[] args) {
        def cwd = Paths.get(System.getProperty('user.dir'))
        log.debug 'Using current working directory [{}]', cwd
        def pipeFile = cwd.resolve('project.pipe')
        if (Files.exists(pipeFile)) {
            log.debug 'Found [project.pipe] file [{}]', pipeFile
            def script = new String(Files.readAllBytes(pipeFile))
            def outputConsumer = System.out
            def registry = getRegistry()
            final PipelineScriptRunner pipeline = new PipelineScriptRunner(registry, outputConsumer, script)
            pipeline.run()
            return 0
        }
        log.warn 'Found no [project.pipe] file in [{}]', cwd
        return 1
    }

    private ServiceRegistry getRegistry() {
        return new ServiceLocator().find(ServiceRegistry)
    }
}
