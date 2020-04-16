/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class OwnerController {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private final ClinicService clinicService;


    @Autowired
    public OwnerController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/owners/new")
    public String initCreationForm(Map<String, Object> model) {
        Owner owner = new Owner();
        model.put("owner", owner);
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/owners/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            this.clinicService.saveOwner(owner);
            return "redirect:/owners/" + owner.getId();
        }
    }

    @GetMapping(value = "/owners/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("owner", new Owner());
        return "owners/findOwners";
    }

@RequestMapping(value = "/owners", method = RequestMethod.GET)
public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {

	// allow parameterless GET request for /owners to return all records
	if (owner.getLastName() == null) {
		owner.setLastName(""); // empty string signifies broadest possible search
	}

	// find owners by last name
	Collection<Owner> results = this.clinicService.findOwnerByLastName(owner.getLastName());
	if (results.isEmpty()) {
		// no owners found
		result.rejectValue("lastName", "notFound", "not found");
		return "owners/findOwners";
	} else if (results.size() == 1) {
		// 1 owner found
		owner = results.iterator().next();
		return "redirect:/owners/" + owner.getId();
	} else {
	      // multiple owners found
		for (Owner ownerItem : results) {
			String initNum = ownerItem.getTelephone().substring(0, 3);
			if (!isInternetTel(initNum)) {
				if ("Seoul".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("02-" + ownerItem.getTelephone());
				} else if ("Busan".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("051-" + ownerItem.getTelephone());
				} else if ("Daegu".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("053-" + ownerItem.getTelephone());
				} else if ("Incheon".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("032-" + ownerItem.getTelephone());
				} else if ("Gwangju".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("062-" + ownerItem.getTelephone());
				} else if ("Daejeon".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("042-" + ownerItem.getTelephone());
				} else if ("Ulsan".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("052-" + ownerItem.getTelephone());
				} else if ("Sejong".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("044-" + ownerItem.getTelephone());
				} else if ("Gyeonggi".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("031-" + ownerItem.getTelephone());
				} else if ("Gangwon".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("033-" + ownerItem.getTelephone());
				} else if ("Chungcheongbuk".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("043-" + ownerItem.getTelephone());
				} else if ("Chungcheongnam".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("041-" + ownerItem.getTelephone());
				} else if ("Jeonbuk".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("063-" + ownerItem.getTelephone());
				} else if ("Jeonnam".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("061-" + ownerItem.getTelephone());
				} else if ("Gyeongbuk".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("054-" + ownerItem.getTelephone());
				} else if ("Gyeongnam".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("055-" + ownerItem.getTelephone());
				} else if ("Jeju".equals(ownerItem.getCity())) {
					ownerItem.setTelephone("064-" + ownerItem.getTelephone());
				}
			}
		}
		model.put("selections", results);
		return "owners/ownersList";
	}
}

private boolean isInternetTel(String initNum) {
	return initNum == "070";
}
    @GetMapping(value = "/owners/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        model.addAttribute(owner);
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/owners/{ownerId}/edit")
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            owner.setId(ownerId);
            this.clinicService.saveOwner(owner);
            return "redirect:/owners/{ownerId}";
        }
    }

    /**
     * Custom handler for displaying an owner.
     *
     * @param ownerId the ID of the owner to display
     * @return a ModelMap with the model attributes for the view
     */
    @GetMapping("/owners/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(this.clinicService.findOwnerById(ownerId));
        return mav;
    }

}
