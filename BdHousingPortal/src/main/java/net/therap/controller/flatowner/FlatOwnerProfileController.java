package net.therap.controller.flatowner;

import net.therap.domain.FlatOwner;
import net.therap.service.FlatOwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ashraf
 * Date: 6/6/12
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/flatownerprofile")
@SessionAttributes("flatownerForm")
public class FlatOwnerProfileController {

    private static final Logger log = LoggerFactory.getLogger(FlatOwnerProfileController.class);

    @Autowired
    private FlatOwnerService flatOwnerService;

    public FlatOwnerService getFlatOwnerService() {
        return flatOwnerService;
    }

    public void setFlatOwnerService(FlatOwnerService flatOwnerService) {
        this.flatOwnerService = flatOwnerService;
    }


    @RequestMapping(value = "/view.htm", method = RequestMethod.GET)
    public String profileViewAction(Map<String, Object> model, HttpServletRequest request) {
        model.put("flatowner", request.getSession().getAttribute("flatowner"));
        return "flatowner/flatownerdetailsview";
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
    }

    @RequestMapping(value = "/edit.htm", method = RequestMethod.GET)
    public String profileEditGetAction(Map<String, Object> model, HttpServletRequest request) {
        FlatOwner flatOwner = flatOwnerService.getFlatOwnerById(((FlatOwner) request.getSession().getAttribute("flatowner")).getFlatOwnerId());
        model.put("flatownerForm",flatOwner);
        model.put("title", "Flat owner Profile edit Form");
        return "flatowner/flatowneredit";
    }

    @RequestMapping(value = "/edit.htm", method = RequestMethod.POST)
    public String profileEditPostAction(@Valid @ModelAttribute("flatownerForm") FlatOwner flatOwner, BindingResult bindingResult, HttpServletRequest request, Map<String, Object> model) {

        if (bindingResult.hasErrors()) {
            model.put("flatownerForm", flatOwner);
            model.put("title", "Customer Profile edit Form");
            return "flatowner/flatowneredit";
        } else {
            log.debug("####" + "InMethodPost" + flatOwner.getFlatOwnerId());
            flatOwnerService.updateFlatOwner(flatOwner);
            request.getSession().removeAttribute("flatownerForm");
            request.getSession().setAttribute("flatowner", flatOwner);
            return "redirect:/own/flatownerprofile/view.htm";

        }

    }
}
