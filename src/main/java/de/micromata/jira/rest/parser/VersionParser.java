/*
 * Micromata GmbH
 * Copyright (c)
 *
 * 23.08.13 09:14
 * connect
 * Christian
 */

package de.micromata.jira.rest.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.micromata.jira.rest.domain.VersionBean;
import de.micromata.jira.rest.util.DateParser;
import de.micromata.jira.rest.util.JsonElementUtil;

/**
 * @author Christian Schulze
 * @author Vitali Filippow
 *
 */
public class VersionParser extends BaseParser {

    public static VersionBean parse(JsonObject object){
        VersionBean bean = new VersionBean();
        parseBaseProperties(bean, object);
        JsonElement descriptionElement = object.get(PROP_DESCRIPTION);
        if(JsonElementUtil.checkNotNull(descriptionElement) == true){
            String description = descriptionElement.getAsString();
            bean.setDescription(description);
        }

        JsonElement releaseDateElement = object.get(PROP_RELEASEDATE);
        if(JsonElementUtil.checkNotNull(releaseDateElement) == true){
            String relesaseDateString = releaseDateElement.getAsString();
            Date releaseDate = DateParser.parseDateFormat(relesaseDateString, DateParser.Format.YYYY_MM_DD);
            bean.setReleaseDate(releaseDate);
        }

        JsonElement archivedElement = object.get(PROP_ARCHIVED);
        if(JsonElementUtil.checkNotNull(archivedElement) == true){
            boolean archived = archivedElement.getAsBoolean();
            bean.setArchived(archived);
        }

        JsonElement releasedElement = object.get(PROP_RELEASED);
        if(JsonElementUtil.checkNotNull(releasedElement) == true){
            boolean released = releasedElement.getAsBoolean();
            bean.setReleased(released);
        }

        JsonElement overdueElement = object.get(PROP_OVERDUE);
        if(JsonElementUtil.checkNotNull(overdueElement) == true){
            boolean overdue = overdueElement.getAsBoolean();
            bean.setOverdue(overdue);
        }

        JsonElement userReleaseDateElement = object.get(PROP_USER_RELEASE_DATE);
        if(JsonElementUtil.checkNotNull(userReleaseDateElement) == true){
            Date date = DateParser.parseDateFormat(userReleaseDateElement.getAsString(), DateParser.Format.DD_MMM_YY);
            bean.setUserReleaseDate(date);
        }
        return bean;
    }

    public static List<VersionBean> parse(List<JsonObject> objects){
        List<VersionBean> retval = new ArrayList<VersionBean>();
        for (JsonObject object : objects) {
            VersionBean versionBean = parse(object);
            retval.add(versionBean);
        }
        return retval;
    }
}

