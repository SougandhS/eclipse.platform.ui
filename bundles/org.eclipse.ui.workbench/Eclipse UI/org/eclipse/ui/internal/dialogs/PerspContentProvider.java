/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.internal.WorkbenchActivityHelper;
import org.eclipse.ui.internal.registry.IPluginContribution;

public class PerspContentProvider implements IStructuredContentProvider {

	/**
	 * PerspContentProvider constructor comment.
	 */
	public PerspContentProvider() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/**
	 * Return the list of perspective descriptors in the supplied registry
	 * filtered for roles if appropriate.
	 * 
	 * @param registry
	 *            the registry to use as the source.
	 * @return IPerspectiveDescriptor[] the active descriptors.
	 */
	IPerspectiveDescriptor[] filteredPerspectives(IPerspectiveRegistry registry) {
		IPerspectiveDescriptor[] descriptors = registry.getPerspectives();
		Collection filtered = new ArrayList();

		for (int i = 0; i < descriptors.length; i++) {
			if (descriptors[i] instanceof IPluginContribution) {
				IPluginContribution contribution =
					(IPluginContribution) descriptors[i];
				if (contribution.fromPlugin()) {
					IIdentifier identifier =
						PlatformUI
							.getWorkbench()
							.getActivityManager()
							.getIdentifier(
							WorkbenchActivityHelper.createUnifiedId(
								contribution));
					if (identifier.isEnabled()) {
						filtered.add(descriptors[i]);
					}
				} else {
					filtered.add(descriptors[i]);
				}
			} else {
				filtered.add(descriptors[i]);
			}
		}

		return (IPerspectiveDescriptor[]) filtered.toArray(
			new IPerspectiveDescriptor[filtered.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object element) {
		if (element instanceof IPerspectiveRegistry) {
			IPerspectiveRegistry reg = (IPerspectiveRegistry) element;
			return filteredPerspectives(reg);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
