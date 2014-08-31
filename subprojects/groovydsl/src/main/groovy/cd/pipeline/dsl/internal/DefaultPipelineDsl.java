package cd.pipeline.dsl.internal;

import cd.pipeline.api.Pipeline;
import cd.pipeline.config.DefaultPipeline;
import cd.pipeline.dsl.StageDsl;
import cd.pipeline.event.PipeEvent;
import cd.pipeline.util.ConfigureUtil;
import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DefaultPipelineDsl implements InternalPipelineDsl {
    private Set<InternalStageDsl> stages = new LinkedHashSet<>();
    private List<PipeEvent> events = new ArrayList<>();
    private boolean hasFailedStage = false;

    @Override
    public StageDsl stage(final String name, final Closure closure) {
        final InternalStageDsl stage = new DefaultStageDsl(name);
        if (hasFailedStage) {
            return stage;
        }
        try {
            ConfigureUtil.configure(stage, closure);
            stages.add(stage);
            events.add(new PipeEvent("ProjectName", "Success", stage.getDescription(), ""));
            return stage;
        } catch (PipelineException e) {
            hasFailedStage = true;
            events.add(new PipeEvent("ProjectName", "Failure", stage.getDescription(), e.getMessage()));
            return stage;
        }
    }

    @Override
    public void echo(final Object value) {
        System.out.print(value);
    }

    @Override
    public void echo(final String format, final Object... values) {
        System.out.printf(format, values);
    }

    @Override
    public Pipeline export() {
        final Pipeline pipeline = new DefaultPipeline();
        for (InternalStageDsl stage : stages) {
            pipeline.add(stage.export());
        }
        return pipeline;
    }
}
