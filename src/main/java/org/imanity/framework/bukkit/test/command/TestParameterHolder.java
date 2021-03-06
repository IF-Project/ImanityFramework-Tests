package org.imanity.framework.bukkit.test.command;

import com.mysql.jdbc.StringUtils;
import org.imanity.framework.*;
import org.imanity.framework.bukkit.test.TestInfo;
import org.imanity.framework.bukkit.test.TestService;
import org.imanity.framework.command.CommandEvent;
import org.imanity.framework.command.parameter.ParameterHolder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ServiceDependency(dependencies = "test", type = @DependencyType(ServiceDependencyType.SUB_DISABLE))
public class TestParameterHolder implements ParameterHolder<TestInfo> {

    @Autowired
    private TestService testService;

    @Override
    public Class[] type() {
        return new Class[] { TestInfo.class };
    }

    @Override
    public TestInfo transform(CommandEvent commandEvent, String source) {
        final TestInfo test = this.testService.getTestByName(source);

        if (test == null) {
            commandEvent.sendInternalError("The Test with name " + source + " Not found!");
            return null;
        }
        return test;
    }

    @Override
    public List<String> tabComplete(Object user, Set<String> flags, String source) {
        return this.testService.getTestByPlugin()
                .stream()
                .map(TestInfo::getName)
                .filter(name -> StringUtils.startsWithIgnoreCase(name, source))
                .collect(Collectors.toList());
    }
}
