/*
 *    SAM-QFS_notice_begin
 *
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the license at pkg/OPENSOLARIS.LICENSE
 * or http://www.opensolaris.org/os/licensing.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at pkg/OPENSOLARIS.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */
/*
 * Copyright 2008 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 *
 *    SAM-QFS_notice_end
 */

// ident        $Id: SharedFSStorageNodeBean.java,v 1.3 2008/06/18 20:28:07 ronaldso Exp $

package com.sun.netstorage.samqfs.web.fs;

import com.sun.data.provider.TableDataProvider;
import com.sun.data.provider.impl.ObjectArrayDataProvider;
import com.sun.netstorage.samqfs.web.model.SamQFSSystemSharedFSManager;
import com.sun.netstorage.samqfs.web.model.SharedHostInfo;
import com.sun.netstorage.samqfs.web.util.Constants;
import com.sun.netstorage.samqfs.web.util.JSFUtil;
import com.sun.web.ui.component.Hyperlink;
import com.sun.web.ui.model.Option;
import com.sun.web.ui.model.OptionTitle;
import javax.servlet.http.HttpServletRequest;

public class SharedFSStorageNodeBean {

    /** Page Menu Options */
    protected String [][] menuOptions = new String [][] {
        {"SharedFS.operation.editmo", "editmo"},
        {"SharedFS.operation.mount", "mount"},
        {"SharedFS.operation.unmount", "unmount"},
        {"SharedFS.operation.enablealloc", "enablealloc"},
        {"SharedFS.operation.disablealloc", "disablealloc"},
        {"SharedFS.operation.clearfaults", "clearfault"},
    };

    // Basic Filter Menu
    protected String [][] filterOptions = new String [][] {
        {"SharedFS.filter.all",
             Short.toString(SamQFSSystemSharedFSManager.FILTER_NONE)},
        {"SharedFS.state.ok",
             Short.toString(SamQFSSystemSharedFSManager.FILTER_OK)},
        {"SharedFS.state.unmounted",
             Short.toString(SamQFSSystemSharedFSManager.FILTER_UNMOUNTED)},
        {"SharedFS.state.allocdisabled",
             Short.toString(SamQFSSystemSharedFSManager.FILTER_DISABLED)},
        {"SharedFS.state.faults",
             Short.toString(SamQFSSystemSharedFSManager.FILTER_FAULTS)}
    };

    private SharedHostInfo [] infos = null;

    public SharedFSStorageNodeBean() {
    }

    public Hyperlink [] getBreadCrumbs(String fsName){
        Hyperlink [] links = new Hyperlink[3];
        for (int i = 0; i < links.length; i++){
            links[i] = new Hyperlink();
            links[i].setImmediate(true);
            links[i].setId("breadcrumbid" + i);
            if (i == 0){
                // Back to FS Summary
                links[i].setText(JSFUtil.getMessage("FSSummary.title"));
                links[i].setUrl(
                    "/fs/FSSummary?" +
                    Constants.PageSessionAttributes.SAMFS_SERVER_NAME + "=" +
                    JSFUtil.getServerName());
            } else if (i == 1){
                // Back to Shared FS Summary
                links[i].setText(JSFUtil.getMessage("SharedFS.title"));
                links[i].setUrl(
                    "SharedFSSummary.jsp?" +
                     Constants.PageSessionAttributes.SAMFS_SERVER_NAME + "=" +
                     JSFUtil.getServerName() + "&" +
                     Constants.PageSessionAttributes.FILE_SYSTEM_NAME + "=" +
                     fsName);
            } else {
                // client / sn page
                links[i].setText(
                    JSFUtil.getMessage("SharedFS.pagetitle.sn"));
            }
        }

        return links;
    }

    public void populate(SharedHostInfo [] infos) {
        this.infos = infos;
    }

    public TableDataProvider getSnList() {
System.out.println("sn: getSnList: info is " +
    (infos != null ? "not " : "") + "null!");
        return
            infos == null?
                new ObjectArrayDataProvider(new SharedHostInfo[0]) :
                new ObjectArrayDataProvider(infos);
    }

    public Option [] getJumpMenuOptions() {
        Option[] jumpMenuOptions = new Option[menuOptions.length + 1];
        jumpMenuOptions[0] =
                new OptionTitle(JSFUtil.getMessage("common.dropdown.header"));
        for (int i = 1; i < menuOptions.length + 1; i++) {
            jumpMenuOptions[i] =
                new Option(
                    menuOptions[i - 1][1],
                    JSFUtil.getMessage(menuOptions[i - 1][0]));
        }

        return jumpMenuOptions;
    }

    public Option [] getFilterMenuOptions() {
        Option[] filterMenuOptions = new Option[filterOptions.length];
        for (int i = 0; i < filterOptions.length; i++) {
            filterMenuOptions[i] =
                new Option(
                    filterOptions[i][1],
                    JSFUtil.getMessage(filterOptions[i][0]));
        }

        return filterMenuOptions;
    }

    /**
     * Method to retrieve page mode from the request, and save it in the
     * session.
     */
    public short getFilter() {
        HttpServletRequest request = JSFUtil.getRequest();
        String mode = request.getParameter("mode");
        try {
            if (mode == null) {
                mode = (String) JSFUtil.getAttribute("mode");
                if (mode == null) {
                    return 0;
                } else {
                    return Short.parseShort(mode);
                }
            } else {
                JSFUtil.setAttribute("mode", mode);
                return Short.parseShort(mode);
            }
        } catch (NumberFormatException numEx) {
            System.out.println("NumberFormatException in getMode()");
            return -1;
        }
    }

    public String getSnTableTitle() {
        if (0 == getFilter()) {
            return JSFUtil.getMessage("SharedFS.title.table.sns");
        // special case because faults == 5, otherwise 0-4 will go well with
        // the last else case
        } else if (SamQFSSystemSharedFSManager.FILTER_FAULTS == getFilter()) {
            return JSFUtil.getMessage(
                "SharedFS.title.table.sns.filtered",
                new String [] {
                    JSFUtil.getMessage(filterOptions[4][0])});
        } else {
            return JSFUtil.getMessage(
                "SharedFS.title.table.sns.filtered",
                new String [] {
                    JSFUtil.getMessage(filterOptions[getFilter()][0])});
        }
    }

    public String getSnTableFilterSelectedOption() {
        if (getFilter() <= 0) {
            return OptionTitle.NONESELECTED;
        } else {
            return Short.toString(getFilter());
        }
    }
}