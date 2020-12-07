package cloud.iswift.framework.maven.plugin.frontend;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.*;

@Mojo(
        name = "frontend",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
        threadSafe = true
)
public class FrontendMojo extends AbstractMojo {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    private static final String SHELL_INSTALL_COMMAND = "yarn install";

    private static final String SHELL_BUILD_COMMAND = "yarn run build";

    @Parameter(property = "frontend.command.install", defaultValue = SHELL_INSTALL_COMMAND)
    protected String installCommand;

    @Parameter(property = "frontend.command.build", defaultValue = SHELL_BUILD_COMMAND)
    protected String buildCommand;

    @Parameter(property = "frontend.source.dir", defaultValue = "${basedir}")
    protected File sourceDir;

    @Parameter(property = "frontend.dist.dir", defaultValue = "${basedir}/dist")
    protected File distDir;

    @Parameter(property = "frontend.target", defaultValue = "static")
    protected String target;

    @Component
    private MavenProject project;

    @java.lang.Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("frontend install & build");
        String installScript = getInstallScript();
        String buildScript = getBuildScript();
        try {
            getLog().info("frontend " + installScript + " begin");
            runCommand(installScript, sourceDir);
            getLog().info("frontend " + installScript + " end");
            getLog().info("frontend " + buildScript + " begin");
            runCommand(buildScript, sourceDir);
            getLog().info("frontend " + buildScript + " end");
        } catch (Exception e) {
            getLog().error("frontend install & build fail");
            getLog().error(e);
            System.exit(1);
        }
        Resource resource = new Resource();

        resource.setDirectory(distDir.getAbsolutePath());

        resource.setTargetPath(target);

        project.addResource(resource);
        getLog().info("frontend install & build success");
    }

    private String getInstallScript() {
        if (isWindow()) {
            return "cmd /c " + installCommand;
        } else {
            return installCommand;
        }
    }

    private String getBuildScript() {
        if (isWindow()) {
            return "cmd /c " + buildCommand;
        } else {
            return buildCommand;
        }
    }


    private boolean isWindow() {
        return OS.indexOf("windows") >= 0;
    }

    private String runCommand(String command, File dir) throws Exception {
        final Process process = Runtime.getRuntime().exec(command, null, dir);
        StringBuffer stringBuffer = new StringBuffer();
        readStream(process.getErrorStream(), stringBuffer);
        readStream(process.getInputStream(), stringBuffer);
        process.waitFor();
        return stringBuffer.toString();
    }

    public Thread readStream(final InputStream inputStream, StringBuffer stringBuffer) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String line;
                    InputStreamReader isr = new InputStreamReader(inputStream, isWindow() ? "GBK" : "UTF8");
                    BufferedReader br = new BufferedReader(isr);
                    while ((line = br.readLine()) != null) {
                        stringBuffer.append(line);
                        getLog().info(line);
                    }
                } catch (IOException e) {
                    getLog().info(e);
                }
            }
        });
        t.start();
        return t;
    }

}
