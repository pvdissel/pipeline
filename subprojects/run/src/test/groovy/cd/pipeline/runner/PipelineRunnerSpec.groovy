package cd.pipeline.runner

import spock.lang.Ignore
import spock.lang.Specification

import java.nio.file.Paths

@Ignore('Needs impl')
class PipelineRunnerSpec extends Specification {

    def 'Should fail when project does not contain a Pipeline config'() {
        given: 'a project directory that does not contain a Pipeline config [project.pipe]'
        def pipeline = PipelineBuilder.builder().withProjectDir(Paths.get(this.class.getResource('/projectWithNoProject.pipe').toURI())).build()
        pipeline.applyFrom this.class.getResource('project.pipe')

        when: 'executing a run'
        def exitCode = new PipelineRunner().run(pipeline)

        then: 'the run fails'
        exitCode != 0
    }

    def 'Should fail when Pipeline config run fails'() {
        given: 'a project directory that contains a failing Pipeline config [project.pipe]'
        def pipeline = PipelineBuilder.builder().withProjectDir(Paths.get(this.class.getResource('/projectWithFailingProject.pipe').toURI())).build()
        pipeline.applyFrom this.class.getResource('project.pipe')

        when: 'it fails during execution'
        def exitCode = new PipelineRunner().run(pipeline)

        then: 'the pipeline run fails'
        exitCode != 0
    }

    def 'Should succeed when Pipeline config run succeeds'() {
        given: 'a project directory that contains a Pipeline config [project.pipe]'
        def pipeline = PipelineBuilder.builder().withProjectDir(Paths.get(this.class.getResource('/projectWithSuccessfulProject.pipe').toURI())).build()
        pipeline.applyFrom this.class.getResource('project.pipe')

        when: 'it executes successfully'
        def exitCode = new PipelineRunner().run(pipeline)

        then: 'the pipeline runs successful'
        exitCode == 0
    }

    def 'Should succeed when Pipeline config run succeeds2222'() {
        given: 'a project directory that contains a Pipeline config [project.pipe]'
        def workingDir = Paths.get(this.class.getResource('/projectWithSuccessfulProject.pipe').toURI())

        def builder = new ProcessBuilder('pipe-run')
        builder.directory(workingDir.toFile())

        when: 'it executes successfully'
        def exitCode = builder.start().exitValue()

        then: 'the pipeline runs successful'
        exitCode == 0
    }
}
