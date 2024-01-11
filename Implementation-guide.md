# Implementation Guide

Much of the benefit of this plugin will be realized when other plugins that
enhance Jenkins offer support for it.

## Add support in your plugin

- Extend the DashboardPortlet class and provide a descriptor that
  extends the `Descriptor<DashboardPortlet>`
- Create a jelly view called *portlet.jelly*
- Optionally create a jelly view called *main.jelly* to be used when the portlet
  is in maximized mode (otherwise the same *portlet.jelly* view will be used)

It is possible to define custom parameters for the DashboardPortlet. The
displayName is always required. To add new parameters:

- create a jelly file called *config.jelly* to be used when the portlet is
  configured (added to the view in 'Edit View' config page);
- modify constructor (with `@DataBoundConstructor`) to receive the new
  parameters.

Looking at the source code of this plugin will show a number of examples
of doing this. The core portlets do the same thing that your plugin
would do.

## Sample files

***MyPortlet.java***

```
import hudson.plugins.view.dashboard.DashboardPortlet;

class MyPortlet extends DashboardPortlet {

    @DataBoundConstructor
    public MyPortlet(String name) {
        super(name);
    }

// do whatever you want

    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {
        @Override
        public String getDisplayName() {
            return "MyPortlet";
        }
    }
};
```

***portlet.jelly***

If you want to show a single table inside the portlet use *dp:decorate-table*. This ensures that the table is properly
rendered and really fills the complete area (The default styling of Jenkins adds rounded borders everywhere and a margin
at the bottom that make it look not so nice).<br/>
You can pass additional classes to be set on the table, e.g. to make it sortable.<br/>
```
<j:jelly xmlns:j="jelly:core" xmlns:dp="/hudson/plugins/view/dashboard">

  <dp:decorate-plain portlet="${it}" class="sortable"> <!-- This is to say that this is a dashboard view portlet for a table-->
  <!-- you can include a separate file with the logic to display your data or you can write here directly -->
    <st:include page="myportlet.jelly"/>
  </dp:decorate-plain>
</j:jelly>
```

To show any other kind of content use *dp:decorate-plain*
```
<j:jelly xmlns:j="jelly:core" xmlns:dp="/hudson/plugins/view/dashboard">

  <dp:decorate-plain portlet="${it}"> <!-- This is to say that this is a dashboard view portlet -->
      <!-- you can include a separate file with the logic to display your data or you can write here directly -->
        <st:include page="myportlet.jelly"/>
  </dp:decorate-plain>
</j:jelly>
```
