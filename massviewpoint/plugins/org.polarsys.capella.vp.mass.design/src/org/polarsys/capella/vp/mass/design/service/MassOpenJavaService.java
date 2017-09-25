/*******************************************************************************
 * Copyright (c) 2006, 2017 Thales Global Services
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Thales - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.mass.design.service;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.polarsys.capella.core.data.capellacore.Type;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.vp.mass.helpers.MassCreationToolHelper;
import org.polarsys.capella.vp.mass.helpers.MassHelper;
import org.polarsys.capella.vp.mass.mass.Mass;
import org.polarsys.capella.vp.mass.mass.PartMass;
import org.polarsys.capella.vp.mass.services.MassCapellaService;
import org.polarsys.kitalpha.emde.model.ElementExtension;

/**
 * <!-- begin-user-doc --> This class is an implementation of the DoReMi
 * JavaExtension '
 * <em><b>[org.polarsys.capella.vp.mass.design.service.MassOpenJavaService]</b></em>
 * '. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class MassOpenJavaService {
	/**
	* <!-- begin-user-doc --> <!-- end-user-doc -->
	* @generated
	*/
	public MassOpenJavaService() {
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param element : the element
	 * @param newSemanticContainer : the element view
	 * @generated NOT
	 */
	public boolean createPC_Mass(EObject element, EObject newSemanticContainer) {
		MassCreationToolHelper massCreationToolHelper = new MassCreationToolHelper();
		massCreationToolHelper.createMass(element, 1);

		return true;
	}

	/**
	 * <!-- begin-user-doc --> 
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<EObject> getMassObjects(EObject eObject, DSemanticDiagram diagram) {
		EList<EObject> result = new BasicEList<EObject>();

		for (DSemanticDecorator node : diagram.getContainers()) {
			final EObject target = node.getTarget();
			if (target instanceof Part) {
				EList<EObject> massElement = getMassObjects(target);
				if (massElement != null && !massElement.isEmpty()) {
					result.addAll(massElement);
				}
			}
		}

		return result;
	}

	/**
	 * <!-- begin-user-doc --> 
	 * <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<EObject> getMassObjects(EObject eObject) {
		EList<EObject> resultat = new BasicEList<EObject>();
		MassHelper massHelper = new MassHelper();
		resultat.addAll(massHelper.getMassObjects(eObject));
		return resultat;
	}

	/**
	 * @generated NOT
	 */
	public boolean massDecoratorPrecondition(EObject element, EObject container) {
		if (container instanceof AbstractDNode && element instanceof Part) {
			Type type = ((Part) element).getType();
			if (type instanceof PhysicalComponent) {
				return getMassObject(element) != null;
			}
		}
		return false;
	}

	/**
	 * @generated NOT
	 */
	public IFigure massTooltip(EObject element) {
		Figure composite = new Figure();
		GridLayout fl = new GridLayout(1, false);
		fl.horizontalSpacing = 1;
		fl.verticalSpacing = 1;
		composite.setLayoutManager(fl);
		// Need to set the size so that it can be correctly displayed
		composite.setSize(35, 20);

		Label text = new Label(
				"Mass\n • Actual value : " + getCurrentMass(element) + "\n • Value : " + getValue(element)
						+ "\n • Min Value : " + getMinValue(element) + "\n • Max Value : " + getMaxValue(element));
		composite.add(text);

		return composite;
	}

	private String getCurrentMass(EObject element) {
		PartMass mass = getMassObject(element);
		return Integer.toString(mass != null ? mass.getCurrentMass() : 0);
	}

	private String getValue(EObject element) {
		PartMass mass = getMassObject(element);
		return Integer.toString(mass != null ? mass.getValue() : 0);
	}

	private String getMinValue(EObject element) {
		PartMass mass = getMassObject(element);
		return Integer.toString(mass != null ? mass.getMinValue() : 0);
	}

	private String getMaxValue(EObject element) {
		PartMass mass = getMassObject(element);
		return Integer.toString(mass != null ? mass.getMaxValue() : 0);
	}

	private PartMass getMassObject(EObject element) {
		if (element instanceof AbstractDNode) {
			return (PartMass) (new MassCapellaService()).getMassObject(((AbstractDNode) element).getTarget());
		}
		return (PartMass) (new MassCapellaService()).getMassObject(element);
	}

	private MassCapellaService maMassService = new MassCapellaService();

	/**
	 * Adapted Weight Services 
	 */
	public int computeMass(EObject eObject) {
		int m = maMassService.compute(eObject, maMassService.getVisitor(), Mass.class);
		((PartMass) maMassService.getMassObject(eObject)).setCurrentMass(m);
		return m;
	}

	public boolean isMassOverhead(EObject eObject, EObject view, EObject container) {
		if (eObject instanceof Mass) {
			return evaluateMassStatus(eObject, MassStatus.OVERHEAD);
		} else if (eObject instanceof Part) {
			return computePartStatus((Part) eObject, view, container, MassStatus.OVERHEAD);
		}
		return false;
	}

	public boolean isMassSaturated(EObject eObject, EObject view, EObject container) {
		if (eObject instanceof Mass) {
			return evaluateMassStatus(eObject, MassStatus.SATURATED);
		} else if (eObject instanceof Part) {
			return computePartStatus((Part) eObject, view, container, MassStatus.SATURATED);
		}
		return false;
	}

	private boolean computePartStatus(Part part, EObject view, EObject container, MassStatus flag) {
		Mass currentPCMass = getMassExtension(part);
		if (currentPCMass != null)
			return evaluateMassStatus(currentPCMass, flag);
		return false;
	}

	private Mass getMassExtension(Part part) {
		EList<ElementExtension> extensions = part.getOwnedExtensions();
		for (ElementExtension elementExtension : extensions) {
			if (elementExtension instanceof Mass)
				return (Mass) elementExtension;
		}
		return null;
	}

	private boolean evaluateMassStatus(EObject eObject, MassStatus flag) {
		final int current = maMassService.computeMass(eObject);
		final int maxValue = ((Mass) eObject).getMaxValue();

		if (maxValue <= 0) {
			return false;
		}

		switch (flag) {
		case OVERHEAD:
			return current > maxValue;

		case SATURATED:
			return current != 0 && current == maxValue;
		}

		//May be a runtimeException
		return false;
	}

	private enum MassStatus {
		OVERHEAD, SATURATED
	}
}