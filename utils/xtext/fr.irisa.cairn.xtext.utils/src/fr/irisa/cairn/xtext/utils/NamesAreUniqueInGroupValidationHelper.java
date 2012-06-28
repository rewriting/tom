package fr.irisa.cairn.xtext.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.NamesAreUniqueValidationHelper;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * A unique validation helper with a notion of namespace groups. In a group all
 * names for the same {@link EClass} are unique. Groups are identified from the
 * {@link IUniqueNamingHelper}. If no helper has been injected then only one
 * group exist.
 * 
 * @author Antoine Floc'h - Initial contribution and API
 * 
 */
public class NamesAreUniqueInGroupValidationHelper extends
		NamesAreUniqueValidationHelper {
	@Inject
	private IUniqueNamingHelper helper;

	public void checkUniqueNames(Iterable<IEObjectDescription> descriptions,
			CancelIndicator cancelIndicator, ValidationMessageAcceptor acceptor) {
		Iterator<IEObjectDescription> iter = descriptions.iterator();
		if (!iter.hasNext())
			return;
		Map<EClass, Set<NamingGroupSingleton>> clusterToNames = Maps
				.newHashMap();
		while (iter.hasNext()) {
			IEObjectDescription description = iter.next();
			checkDescriptionForDuplicatedNameInGroup(description,
					clusterToNames, acceptor);
			if (cancelIndicator != null && cancelIndicator.isCanceled())
				return;
		}
	}

	protected void checkDescriptionForDuplicatedNameInGroup(
			IEObjectDescription description,
			Map<EClass, Set<NamingGroupSingleton>> clusterToNames,
			ValidationMessageAcceptor acceptor) {

		EObject object = description.getEObjectOrProxy();
		EClass eClass = object.eClass();
		EClass clusterType = getAssociatedClusterType(eClass);

		Set<NamingGroupSingleton> groupsForClusterType = clusterToNames
				.get(clusterType);
		if (groupsForClusterType == null) {
			groupsForClusterType = new HashSet<NamesAreUniqueInGroupValidationHelper.NamingGroupSingleton>();
			clusterToNames.put(clusterType, groupsForClusterType);
		}
		NamingGroupSingleton group = getGroupSingleton(
				description.getEObjectOrProxy(), groupsForClusterType);

		if (group.check(description)) {
			group.add(description);
		} else {
			createDuplicateNameError(group.getConflictDescription(description),
					clusterType, acceptor);
			createDuplicateNameError(description, clusterType, acceptor);
		}

	}

	private NamingGroupSingleton getGroupSingleton(EObject o,
			Set<NamingGroupSingleton> set) {
		NamingGroupSingleton g = new NamingGroupSingleton(o);
		for (NamingGroupSingleton og : set) {
			if (og.equals(g))
				return og;
		}
		set.add(g);
		return g;
	}

	private class NamingGroupSingleton {

		private EObject object;
		private Map<String, IEObjectDescription> descriptions = Maps
				.newHashMap();

		public NamingGroupSingleton(EObject object) {
			this.object = object;
		}

		/**
		 * Test if a description is valid with the group.
		 * 
		 * @param description
		 * @return true if no description exist with the same name
		 */
		public boolean check(IEObjectDescription description) {
			String name = description.getQualifiedName().toString();
			return !descriptions.containsKey(name);
		}

		public IEObjectDescription getConflictDescription(
				IEObjectDescription description) {
			return descriptions.get(description.getQualifiedName());
		}

		public void add(IEObjectDescription description) {
			descriptions.put(description.getQualifiedName().toString(), description);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NamingGroupSingleton other = (NamingGroupSingleton) obj;
			if (object == null) {
				if (other.object != null)
					return false;
			} else if (helper != null
					&& !helper.areInSameNamingGroup(object, other.object))
				return false;
			return true;
		}

	}

}
