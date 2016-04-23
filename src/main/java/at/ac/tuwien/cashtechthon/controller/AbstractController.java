package at.ac.tuwien.cashtechthon.controller;

/**
 * Abstract base class for all MVC controllers
 */
public abstract class AbstractController {

    protected abstract String getViewDir();

    protected String createViewPath(String viewName) {
        return getViewDir() + "/" + viewName;
    }
}
