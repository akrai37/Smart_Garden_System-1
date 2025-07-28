package com.example.ooad_project;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name = "LoggerAppender", category = "Core", elementType = Appender.ELEMENT_TYPE, printObject = true)
public class LoggerAppender extends AbstractAppender {

    // Reference to the garden UI controller for displaying log messages in the interface
    private static GardenUIController gardenControllerReference;

    // Constructor that sets up the log appender with a name and formatting layout
    protected LoggerAppender(String loggerIdentifier, Layout<?> messageFormatter) {
        super(loggerIdentifier, null, messageFormatter, true, null);
    }

    /**
     * Factory method to create a new LoggerAppender instance
     * This method is called by Log4j2 when creating appenders from configuration
     * 
     * @param appenderName The unique name for this appender
     * @param layoutConfiguration The layout that defines how log messages are formatted
     * @return A new LoggerAppender instance, or null if creation fails
     */
    @PluginFactory
    public static LoggerAppender createAppender(@PluginAttribute("name") String appenderName,
                                                @PluginElement("Layout") Layout<?> layoutConfiguration) {
        // Validate that we have a proper name for the appender
        if (appenderName == null) {
            LOGGER.error("No name provided for LoggerAppender");
            return null;
        }

        // Use default layout if none was provided
        if (layoutConfiguration == null) {
            layoutConfiguration = PatternLayout.createDefaultLayout();
        }

        return new LoggerAppender(appenderName, layoutConfiguration);
    }

    /**
     * This method is called whenever a log event needs to be processed
     * Currently disabled but would send log messages to the garden UI
     * 
     * @param logEvent The log event containing the message and metadata
     */
    @Override
    public void append(LogEvent logEvent) {
//        if (gardenControllerReference != null) {
//            String message = new String(getLayout().toByteArray(logEvent));
//            gardenControllerReference.appendLogText(message.trim());
//        }
    }

    /**
     * Sets the garden UI controller that will receive log messages
     * This allows the appender to display logs in the user interface
     * 
     * @param gardenController The garden UI controller instance
     */
    public static void setController(GardenUIController gardenController) {
//        LoggerAppender.gardenControllerReference = gardenController;
    }
}
