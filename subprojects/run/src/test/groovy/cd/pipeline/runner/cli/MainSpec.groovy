package cd.pipeline.runner.cli

import org.junit.Rule
import org.junit.contrib.java.lang.system.ExpectedSystemExit
import org.junit.contrib.java.lang.system.internal.CheckExitCalled
import spock.lang.Ignore
import spock.lang.Specification

import java.nio.file.Paths

class MainSpec extends Specification {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    private String originalUserDir = System.getProperty('user.dir')

    void cleanup() {
        System.setProperty('user.dir', originalUserDir)
    }

    def 'Should execute successfully on a directory containing project.pipe file'() {
        given: 'Current working directory contains a project.pipe file'
        withWorkingDir('/projectWithEmptyProject.pipe')

        when: 'app is executed without arguments'
        Main.main()

        then: 'app exit with success exitStatus'
        exit.expectSystemExitWithStatus(0)
        thrown(CheckExitCalled)
    }

    def 'Should not execute successfully on a directory that does not contain project.pipe file'() {
        given: 'Current working directory without a project.pipe file'
        withWorkingDir('/projectWithNoProject.pipe')

        when: 'app is executed without arguments'
        Main.main()

        then: 'app exit with failure exitStatus'
        exit.expectSystemExitWithStatus(1)
        thrown(CheckExitCalled)
    }

    @Ignore('Exception is correctly thrown in DSL, but catched somewhere higher up')
    def 'Should not execute successfully on a directory containing project.pipe file that fails during its run'() {
        given: 'Current working directory contains a project.pipe file'
        withWorkingDir('/projectWithFailingProject.pipe')

        when: 'app is executed without arguments'
        Main.main()

        then: 'app exit with failure exitStatus'
        exit.expectSystemExitWithStatus(1)
        thrown(CheckExitCalled)
    }

    private void withWorkingDir(String path) {
        def string = Paths.get(this.class.getResource(path).toURI()).toString()
        System.setProperty('user.dir', string)
    }
}
