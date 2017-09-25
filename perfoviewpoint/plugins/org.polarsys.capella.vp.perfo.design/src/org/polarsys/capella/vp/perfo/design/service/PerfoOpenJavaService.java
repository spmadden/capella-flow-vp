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
package org.polarsys.capella.vp.perfo.design.service;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.vp.perfo.perfo.PerfoFactory;
import org.polarsys.capella.vp.perfo.perfo.TimeCapacity;
import org.polarsys.capella.vp.perfo.perfo.TimeConsumption;
import org.polarsys.capella.vp.perfo.services.PerformanceServices;
import org.polarsys.kitalpha.emde.model.ElementExtension;
import org.polarsys.kitalpha.emde.model.ExtensibleElement;

/**
 * <!-- begin-user-doc -->
 * This class is an implementation of the DoReMi JavaExtension '<em><b>[org.polarsys.capella.vp.perfo.design.service.PerfoOpenJavaService]</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class PerfoOpenJavaService {
	/**
	* <!-- begin-user-doc -->
	* <!-- end-user-doc -->
	* @generated
	*/
	public PerfoOpenJavaService() {
	}

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param element : the element
     * @param newSemanticContainer : the element view
     * @generated NOT
     */
    public boolean CreateLABFunctionTimeConsumption(EObject element, EObject newSemanticContainer) {
        if (element instanceof AbstractFunction
                && checktimeConsumptionUnicity(((AbstractFunction) element).getOwnedExtensions())) {
            AbstractFunction functionalExchange = (AbstractFunction) element;
            TimeConsumption timeConsumption = PerfoFactory.eINSTANCE.createTimeConsumption();
            timeConsumption.setId(EcoreUtil.generateUUID());
            timeConsumption.setValue(0);
            functionalExchange.getOwnedExtensions().add(timeConsumption);
            return true;
        }

        if (element instanceof FunctionalExchange
                && checktimeConsumptionUnicity(((FunctionalExchange) element).getOwnedExtensions())) {
            FunctionalExchange functionalExchange = (FunctionalExchange) element;
            TimeConsumption timeConsumption = PerfoFactory.eINSTANCE.createTimeConsumption();
            timeConsumption.setId(EcoreUtil.generateUUID());
            timeConsumption.setValue(0);
            functionalExchange.getOwnedExtensions().add(timeConsumption);
            return true;
        }

        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param element : the element
     * @param newSemanticContainer : the element view
     * @generated NOT
     */
    public boolean createFunctionalCapacity(EObject element, EObject newSemanticContainer) {
        if (element instanceof FunctionalChain
                && checktimeCapacityUnicity(((FunctionalChain) element).getOwnedExtensions())) {
            FunctionalChain functionalChain = (FunctionalChain) element;
            TimeCapacity timeCapacity = PerfoFactory.eINSTANCE.createTimeCapacity();
            timeCapacity.setId(EcoreUtil.generateUUID());
            timeCapacity.setValue(0);
            functionalChain.getOwnedExtensions().add(timeCapacity);
            return true;
        }
        return false;
    }

    private boolean checktimeConsumptionUnicity(EList<ElementExtension> extensions) {

        for (ElementExtension elementExtension : extensions) {
            if (elementExtension instanceof TimeConsumption)
                return false;
        }
        return true;
    }

    private boolean checktimeCapacityUnicity(EList<ElementExtension> extensions) {

        for (ElementExtension elementExtension : extensions) {
            if (elementExtension instanceof TimeCapacity)
                return false;
        }
        return true;
    }

	public EList<EObject> getTimeConsumptionObject(EObject eObject, DSemanticDiagram diagram) {
		EList<EObject> result = new BasicEList<EObject>();

		for (DNode node : diagram.getNodes()) {
			final EObject target = node.getTarget();
			if (target instanceof AbstractFunction) {
				EObject timeConsumption = getTimeConsumptionObject(target);
				if (timeConsumption != null) {
					result.add(timeConsumption);
				}
			}
		}

		for (DEdge edge : diagram.getEdges()) {
			if (edge.getTarget() instanceof FunctionalExchange) {
				EObject timeConsumption = getTimeConsumptionObject(edge.getTarget());
				if (timeConsumption != null) {
					result.add(timeConsumption);
				}
			}
		}

		return result;
	}

	public EObject getTimeConsumptionObject(EObject eO) {
		if (eO == null)
			return null;

		if (eO instanceof ExtensibleElement) {
			ExtensibleElement logicalFunction = (ExtensibleElement) eO;
			EList<ElementExtension> extensions = logicalFunction.getOwnedExtensions();
			for (ElementExtension elementExtension : extensions) {
				if (elementExtension instanceof TimeConsumption) {
					return elementExtension;
				}

			}
		}
		return null;
	}

	public EList<EObject> retrieveMaTimeCapacity(EObject eObject, DSemanticDiagram diagram) {

		EList<EObject> result = new BasicEList<EObject>();

		for (DNode node : diagram.getNodes()) {
			final EObject target = node.getTarget();
			if (target instanceof FunctionalChain) {
				EObject capacity = retrieveMaTimeCapacity_(target);
				if (capacity != null) {
					result.add(capacity);
				}
			}
		}

		for (DEdge edge : diagram.getEdges()) {
			if (edge.getTarget() instanceof FunctionalChain) {
				EObject capacity = retrieveMaTimeCapacity_(edge.getTarget());
				if (capacity != null) {
					result.add(capacity);
				}
			}
		}

		return result;

	}

	public EObject retrieveMaTimeCapacity_(EObject eObject) {
		if (eObject == null)
			return null;

		if (eObject instanceof ExtensibleElement) {
			ExtensibleElement functionalChain = (ExtensibleElement) eObject;
			EList<ElementExtension> extensions = functionalChain.getOwnedExtensions();
			for (ElementExtension elementExtension : extensions) {
				if (elementExtension instanceof TimeCapacity) {
					return elementExtension;
				}

			}
		}
		return null;
	}

	/**
	 * @generated NOT
	 */
	public boolean perfoDecoratorPrecondition(EObject element, EObject container) {
		if (container instanceof AbstractDNode && element instanceof AbstractFunction) {
			return retrieveTimeConsumptionObject(element) != null;
		} else if (container instanceof DEdge && element instanceof FunctionalExchange) {
			return retrieveTimeConsumptionObject(element) != null;
		}
		return false;
	}

	/**
	 * @generated NOT
	 */
	public boolean perfoFCDecoratorPrecondition(EObject element, EObject container) {
		if (container instanceof AbstractDNode && element instanceof FunctionalChain) {
			return getTimeCapacityObject(element) != null;
		}
		return false;
	}

	/**
	 * @generated NOT
	 */
	public IFigure perfoTooltip(EObject element) {
		Figure composite = new Figure();
		GridLayout fl = new GridLayout(1, false);
		fl.horizontalSpacing = 1;
		fl.verticalSpacing = 1;
		composite.setLayoutManager(fl);
		// Need to set the size so that it can be correctly displayed
		composite.setSize(35, 20);

		Label text = new Label("Time Consumption\n • Value : " + getConsumptionValue(element) + "\n • Min Value : "
				+ getConsumptionMinValue(element) + "\n • Max Value : " + getConsumptionMaxValue(element));
		composite.add(text);

		return composite;
	}

	/**
	 * @generated NOT
	 */
	public IFigure perfoFCTooltip(EObject element) {
		Figure composite = new Figure();
		GridLayout fl = new GridLayout(1, false);
		fl.horizontalSpacing = 1;
		fl.verticalSpacing = 1;
		composite.setLayoutManager(fl);
		// Need to set the size so that it can be correctly displayed
		composite.setSize(35, 20);

		Label text = new Label("Time Capacity\n • Current Execution Time : " + getExecutionTime(element)
				+ "\n • Value : " + getCapacityValue(element) + "\n • Min Value : " + getCapacityMinValue(element)
				+ "\n • Max Value : " + getCapacityMaxValue(element));
		composite.add(text);

		return composite;
	}

	private String getConsumptionValue(EObject element) {
		TimeConsumption time = retrieveTimeConsumptionObject(element);
		return Integer.toString(time != null ? time.getValue() : 0);
	}

	private String getConsumptionMinValue(EObject element) {
		TimeConsumption time = retrieveTimeConsumptionObject(element);
		return Integer.toString(time != null ? time.getMinValue() : 0);
	}

	private String getConsumptionMaxValue(EObject element) {
		TimeConsumption time = retrieveTimeConsumptionObject(element);
		return Integer.toString(time != null ? time.getMaxValue() : 0);
	}

	private String getExecutionTime(EObject element) {
		TimeCapacity time = getTimeCapacityObject(element);
		return Integer.toString(time != null ? time.getCurrentExecutionTime() : 0);
	}

	private String getCapacityValue(EObject element) {
		TimeCapacity time = getTimeCapacityObject(element);
		return Integer.toString(time != null ? time.getValue() : 0);
	}

	private String getCapacityMinValue(EObject element) {
		TimeCapacity time = getTimeCapacityObject(element);
		return Integer.toString(time != null ? time.getMinValue() : 0);
	}

	private String getCapacityMaxValue(EObject element) {
		TimeCapacity time = getTimeCapacityObject(element);
		return Integer.toString(time != null ? time.getMaxValue() : 0);
	}

	private TimeConsumption retrieveTimeConsumptionObject(EObject element) {
		if (element instanceof DSemanticDecorator) {
			return (TimeConsumption) getTimeConsumptionObject(((DSemanticDecorator) element).getTarget());
		}
		return (TimeConsumption) getTimeConsumptionObject(element);
	}

	public TimeCapacity getTimeCapacityObject(EObject element) {
		if (element instanceof DSemanticDecorator) {
			return (TimeCapacity) retrieveMaTimeCapacity_(((DSemanticDecorator) element).getTarget());
		}
		return (TimeCapacity) retrieveMaTimeCapacity_(element);
	}

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     **/
    private static PerformanceServices performanceService = new PerformanceServices();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param eObject : the current semantic object
     * @param view : the current view
     * @param container : the semantic container of the current object
     * @generated NOT
     */
    public boolean performanceSaturated(EObject eObject, EObject view, EObject container) {
        TimeCapacity time = (new PerfoOpenJavaService()).getTimeCapacityObject(eObject);
        if (time != null) {
            int checkPerformance = performanceService.checkPerformance(time, eObject);
            int value = time.getValue();
            return value != 0 && value == checkPerformance;
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param eObject : the current semantic object
     * @param view : the current view
     * @param container : the semantic container of the current object
     * @generated NOT
     */
    public boolean performanceOverhead(EObject eObject, EObject view, EObject container) {
        TimeCapacity time = (new PerfoOpenJavaService()).getTimeCapacityObject(eObject);
        if (time != null) {
            int checkPerformance = performanceService.checkPerformance(time, eObject);
            int value = time.getValue();
            return value != 0 && value < checkPerformance;
        }
        return false;
    }
}