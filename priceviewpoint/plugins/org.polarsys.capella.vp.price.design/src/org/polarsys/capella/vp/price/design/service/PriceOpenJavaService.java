/******************************************************************************
* Copyright (c) 2006, 2017 Thales Global Services 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *    Thales - initial API and implementation
*****************************************************************************/
package org.polarsys.capella.vp.price.design.service;

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
import org.polarsys.capella.vp.price.helpers.PriceCreationToolHelper;
import org.polarsys.capella.vp.price.helpers.PriceHelper;
import org.polarsys.capella.vp.price.price.PartPrice;
import org.polarsys.capella.vp.price.price.Price;
import org.polarsys.capella.vp.price.services.PriceCapellaService;
import org.polarsys.kitalpha.emde.model.ElementExtension;

/**
 * <!-- begin-user-doc -->
 * This class is an implementation of the DoReMi JavaExtension '<em><b>[org.polarsys.capella.vp.price.design.service.PriceOpenJavaService]</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */

public class PriceOpenJavaService {
	/**
	* <!-- begin-user-doc -->
	* <!-- end-user-doc -->
	* @generated
	*/
	public PriceOpenJavaService() {
	}

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param element : the element
     * @param newSemanticContainer : the element view
     * @generated NOT
     */
    public boolean createPC_Price(EObject element, EObject newSemanticContainer) {
        PriceCreationToolHelper priceCreationToolHelper = new PriceCreationToolHelper();
        priceCreationToolHelper.createPrice(element, 2);

        return true;
    }

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<EObject> getPriceObjects(EObject eObject, DSemanticDiagram diagram) {
		EList<EObject> result = new BasicEList<EObject>();

		for (DSemanticDecorator node : diagram.getContainers()) {
			final EObject target = node.getTarget();
			if (target instanceof Part) {
				EList<EObject> priceElement = getPriceObjects(target);
				if (priceElement != null && !priceElement.isEmpty()) {
					result.addAll(priceElement);
				}
			}
		}

		return result;
	}

	/**
	 * return the Price object of a given eObject
	 * @param eObject
	 * @return
	 * @generated NOT
	 */
	public EList<EObject> getPriceObjects(EObject eObject) {
		EList<EObject> resultat = new BasicEList<EObject>();
		PriceHelper priceHelper = new PriceHelper();
		resultat.addAll(priceHelper.getPriceObject(eObject));
		return resultat;
	}

	/**
	 * @generated NOT
	 */
	public boolean priceDecoratorPrecondition(EObject element, EObject container) {
		if (container instanceof AbstractDNode && element instanceof Part) {
			Type type = ((Part) element).getType();
			if (type instanceof PhysicalComponent) {
				return getPriceObject(element) != null;
			}
		}
		return false;
	}

	/**
	 * @generated NOT
	 */
	public IFigure priceTooltip(EObject element) {
		Figure composite = new Figure();
		GridLayout fl = new GridLayout(1, false);
		fl.horizontalSpacing = 1;
		fl.verticalSpacing = 1;
		composite.setLayoutManager(fl);
		// Need to set the size so that it can be correctly displayed
		composite.setSize(35, 20);

		Label text = new Label(
				"Price\n • Actual value : " + getCurrentPrice(element) + "\n • Value : " + getValue(element)
						+ "\n • Min Value : " + getMinValue(element) + "\n • Max Value : " + getMaxValue(element));
		composite.add(text);

		return composite;
	}

	private String getCurrentPrice(EObject element) {
		PartPrice price = getPriceObject(element);
		return Integer.toString(price != null ? price.getCurrentPrice() : 0);
	}

	private String getValue(EObject element) {
		PartPrice price = getPriceObject(element);
		return Integer.toString(price != null ? price.getValue() : 0);
	}

	private String getMinValue(EObject element) {
		PartPrice price = getPriceObject(element);
		return Integer.toString(price != null ? price.getMinValue() : 0);
	}

	private String getMaxValue(EObject element) {
		PartPrice price = getPriceObject(element);
		return Integer.toString(price != null ? price.getMaxValue() : 0);
	}

	private PartPrice getPriceObject(EObject element) {
		if (element instanceof AbstractDNode) {
			return (PartPrice) (new PriceCapellaService()).getPriceObject(((AbstractDNode) element).getTarget());
		}
		return (PartPrice) (new PriceCapellaService()).getPriceObject(element);
	}
    
    private PriceCapellaService maPriceService = new PriceCapellaService();
    
    
    /**
     * Adapted Weight Services 
     */
    
    public int computePrice(EObject eObject) {
        int m = maPriceService.computePrice(eObject);
        ((PartPrice)maPriceService.getPriceObject(eObject)).setCurrentPrice(m);
        return m;
    }
    
    public boolean isPriceOverhead(EObject eObject, EObject view, EObject container) {
        if (eObject instanceof Price){
            return evaluatePriceStatus(eObject, PriceStatus.OVERHEAD);
        } else if (eObject instanceof Part) {
            return computePartStatus((Part)eObject, view, container, PriceStatus.OVERHEAD);
        }
        return false;
    }

    public boolean isPriceSaturated(EObject eObject, EObject view, EObject container) {
        if (eObject instanceof Price){
            return evaluatePriceStatus(eObject, PriceStatus.SATURATED);
        } else if (eObject instanceof Part) {
            return computePartStatus((Part)eObject, view, container, PriceStatus.SATURATED);
        }
        return false;
    }

    private boolean computePartStatus(Part part, EObject view, EObject container, PriceStatus flag) {
        Price currentPCPrice = getPriceExtension(part);
        if (currentPCPrice != null)
            return evaluatePriceStatus(currentPCPrice, flag);
        
        return false;
    }

    private Price getPriceExtension(Part part) {
        
        EList<ElementExtension> extensions = part.getOwnedExtensions();
        
        for (ElementExtension elementExtension : extensions) {
            if (elementExtension instanceof Price)
                return (Price)elementExtension;
        }
        
        return null;
    }
    
    
    private boolean evaluatePriceStatus(EObject eObject, PriceStatus flag){
        final int current = maPriceService.computePrice(eObject);
        final int maxValue = ((Price) eObject).getMaxValue();
        
        if (maxValue<= 0) {
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
    
    private enum PriceStatus {
        OVERHEAD,
        SATURATED
    }
}